package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.CommentDto;
import messageboard.Exception.*;
import messageboard.entity.Board;
import messageboard.entity.Comment;
import messageboard.entity.member.Member;
import messageboard.event.ViewsEvent;
import messageboard.service.BardLikeService;
import messageboard.service.BoardService;
import messageboard.service.CommentService;
import messageboard.service.MemberService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService  boardService;
    private final CommentService commentService;
    private final ApplicationEventPublisher eventPublisher;
    private final BardLikeService boardLikeService;
    private final MemberService memberService;

    @GetMapping("/board")
    public String board(Model model, @PageableDefault(size = 10) Pageable pageable,@RequestParam(required = false, defaultValue = "") String title,HttpSession session){
        loginCheck(model);

        Page<Board> boards = boardService.search(title, pageable);
        List<Board> boardAll = boards.getContent();


        int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
        int totalPages = boards.getTotalPages(); // 전체 페이지 수

        int visiblePages = 3;

        int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
        int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

        if(boardAll !=null) {
            model.addAttribute("boardAll",boardAll);
        }

        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);
        model.addAttribute("page",boards);

        return "board/board";
    }



    @GetMapping("/boardWrit")
    public String write(Model model, HttpSession session){
        loginCheck(model);
        model.addAttribute("board",new BoardDto());
        return "board/wirteboard";
    }
    @PostMapping("/api/boardWrit")
    @ResponseBody
    public ResponseEntity<?> writing(@Valid @RequestBody BoardDto boardDto, BindingResult result){
        try {
            if (result.hasErrors()){
                Map<String,String> errorMessage = new HashMap<>();
                for (FieldError fieldError : result.getFieldErrors()) {
                    errorMessage.put(fieldError.getField(), fieldError.getDefaultMessage());
                }
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }
            boardService.save(boardDto);

            return ResponseEntity.ok("성공");

        }catch (Login_RestException e){
            e.printStackTrace();
            throw new Login_RestException("회원가입한 사용자만 사용가능합니다.");
        }
    }

    @GetMapping("/board/{boardId}")
    public String boardInfo(@PathVariable(name = "boardId") Long boardId,Model model,HttpSession session){

            Board byBoardId = boardService.findByBoardId(boardId);
            eventPublisher.publishEvent(new ViewsEvent(byBoardId));     //조회시 카운터 증가

            List<Comment> allComment = commentService.findAllComment(boardId);      //조회수 값조회

        Member loginMember = loginCheck(model);

        boolean boardCheck = false;

            if (loginMember != null) {
                boardCheck = boardLikeService.isBoardCheck(loginMember, boardId);
            }


            model.addAttribute("board_like_check", boardCheck);
            model.addAttribute("allComment", allComment);
            model.addAttribute("comment", new CommentDto());
            model.addAttribute("board", byBoardId);
            return "board/boardInfo";

    }

    @DeleteMapping("/api/delete")
    @ResponseBody
    public ResponseEntity<?> boardDelete(@RequestBody BoardDto boardDto){
        try{

            boardLikeService.deleteByBoardId(boardDto);
            boolean deleteBoard = boardService.deleteBoard(boardDto);
            if (!deleteBoard){
                throw new Login_RestException();
            }
            return ResponseEntity.ok("삭제성공");
        }catch (NotFindPageException e){
            throw new NotFindPage_RestException("게시글이 존재하지 않습니다.");
        }catch (Login_RestException e){
            throw new Login_RestException("작성자만 삭제할 수 있습니다.");
        }
    }


    /**
     *등록된 사용자많이 게시글을 수정할수 있다.
     */
    @GetMapping("/board/update/{id}")
    public String updateGetBoard(@PathVariable(name = "id") Long id, Model model, HttpSession session){
        Board byBoardId = boardService.findByBoardId(id);

        String board_write_user = byBoardId.getMember().getUsername();
        Member loginMember = loginCheck(model);
        if (loginMember.getUsername().equals(board_write_user)) {
            model.addAttribute("board",byBoardId);
        }else
            throw new LoginException("작성자만 수정할수 있습니다.");

        return "board/updateBoard";
    }

    @PutMapping("/api/board/update")
    @ResponseBody
    public ResponseEntity<?> updateBoardAfter(@RequestBody BoardDto boardDto){
        try{
            Board board = boardService.updateBoard(boardDto);

            return ResponseEntity.ok(board);
        }catch (NotFindPageException e){
            throw new NotFindPage_RestException("해당 게시물을 찾을수 없습니다.");
        }catch (Login_RestException e){
            throw new Login_RestException("작성자만 수정 가능합니다.");
        }
    }

    // TODO: 2024-02-20 좋아요, 댓글 삭제, 수정, 유니크 제약 조건 수정하기
    @PostMapping("/api/board/likes")
    @ResponseBody
    public ResponseEntity<?> boardLikes(@RequestBody BoardDto boardDto){
        try{
            log.info("boardDto={}",boardDto);
            int num = boardService.board_like(boardDto);
            return ResponseEntity.ok(num);
        }catch (Login_RestException e) {
            e.printStackTrace();
           throw new Login_RestException("로그인후 이용할수 있습니다.");
        }
    }

    @PostMapping("/api/password/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@RequestBody BoardDto boardDto){
        try {

            Integer integer = boardService.passwordVerify(boardDto);

            if (integer==2){
               throw new Login_RestException();
            } else if (integer==1){
                return ResponseEntity.ok(integer);
            }
           throw new BadRequestException();

        }catch (Login_RestException e) {
            throw new Login_RestException("작성자만 삭제 가능합니다.");
        }catch (BadRequestException e){
            throw new BadRequestException("비밀번호가 일치하지 않습니다.");
        }catch (NotFindPageException e){
            throw new NotFindPage_RestException("해당 게시물이 더이상 존재하지 않습니다.");
        }
    }
    private Member loginCheck(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        Member member =null;

        if (authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            member = memberService.findByLoginId(user.getUsername());
            model.addAttribute("member",member);
        }

        if (authentication.getPrincipal() instanceof OAuth2User) {
            String name = authentication.getName();
            log.info("지금은 OauthUser 로그인 입니다.");

            Map<String,String> loginMember = ((OAuth2User) authentication.getPrincipal()).getAttribute("response");
            if(loginMember!=null) {
                String NaverId = loginMember.get("id");
                if (NaverId != null) {
                    log.info("네이버 로그인실행");
                    member = memberService.findByLoginId(NaverId);
                    model.addAttribute("member", member);
                }
            }
            member = memberService.findByLoginId(name);
            model.addAttribute("member", member);

            log.info("지금은 name={}",name);
        }

        return member;
    }
}
