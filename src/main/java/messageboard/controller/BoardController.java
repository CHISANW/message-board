package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.CommentDto;
import messageboard.Exception.LoginException;
import messageboard.entity.Board;
import messageboard.entity.Comment;
import messageboard.entity.Member;
import messageboard.event.ViewsEvent;
import messageboard.service.Impl.BoardLikeServiceImpl;
import messageboard.service.Impl.BoardServiceImpl;
import messageboard.service.Impl.CommentServiceImpl;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardServiceImpl boardService;
    private final CommentServiceImpl commentService;
    private final ApplicationEventPublisher eventPublisher;
    private final BoardLikeServiceImpl boardLikeService;
    private final MemberServiceImpl memberService;

    @GetMapping("/board")
    public String board(Model model, @PageableDefault(size = 4) Pageable pageable,@RequestParam(required = false, defaultValue = "") String title,HttpSession session){
        Page<Board> boards = boardService.search(title, pageable);
        List<Board> boardAll = boards.getContent();

        Member loginMember = getSession(model, session);



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
        getSession(model, session);
        model.addAttribute("board",new BoardDto());
        return "board/wirteboard";
    }



    @PostMapping("/boardWrit")
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

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류발생");
        }
    }

    @GetMapping("/board/{boardId}")
    public String boardInfo(@PathVariable(name = "boardId") Long boardId,Model model,HttpSession session){
        Board byBoardId = boardService.findByBoardId(boardId);
        eventPublisher.publishEvent(new ViewsEvent(byBoardId));     //조회시 카운터 증가

        List<Comment> allComment = commentService.findAllComment(boardId);      //조회수 값조회

        Member loginMember = getSession(model, session);

        if(loginMember.getUsername()!=null){
            boolean boardCheck = boardLikeService.isBoardCheck(loginMember.getUsername());
            model.addAttribute("board_like_check",boardCheck);
        }

//        if (loginMember.getUsername() == null) {
//           throw new RuntimeException("널");
//        }
//        boolean boardCheck = boardLikeService.isBoardCheck(loginMember.getUsername());
//        model.addAttribute("board_like_check",boardCheck);
        model.addAttribute("allComment",allComment);
        model.addAttribute("comment",new CommentDto());

        model.addAttribute("board",byBoardId);
        return "board/boardInfo";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> boardDelete(@RequestBody BoardDto boardDto){
        try{
            Long id = boardDto.getId();
            String memberUsername = boardDto.getMemberDto().getUsername();
            boolean deleteBoard = boardService.deleteBoard(id, memberUsername);
            if (!deleteBoard){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록된 사용자만 삭제가능");
            }
            return ResponseEntity.ok("삭제성공");
        }catch (EntityNotFoundException e){
            e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물이 존재하지 않습니다.");
        }
    }


    /**
     *등록된 사용자많이 게시글을 수정할수 있다.
     */
    @GetMapping("/board/update/{id}")
    public String updateGetBoard(@PathVariable(name = "id") Long id, Model model, HttpSession session){
        Board byBoardId = boardService.findByBoardId(id);

        String board_write_user = byBoardId.getMember().getUsername();
        Member loginMember = getSession(model, session);
        if (loginMember.getUsername().equals(board_write_user)) {
            model.addAttribute("board",byBoardId);
        }else
            throw new LoginException("로그인한 사용자만 사용할수 있습니다.");

        return "board/updateBoard";
    }

    @PutMapping("/board/update")
    @ResponseBody
    public ResponseEntity<?> updateBoardAfter(@RequestBody BoardDto boardDto){
        try{
            Board board = boardService.updateBoard(boardDto);
            return ResponseEntity.ok(board);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류 발생");
        }
    }

    @PostMapping("/board/likes")
    @ResponseBody
    public ResponseEntity<?> boardLikes(@RequestBody BoardDto boardDto){
        try{
            int num = boardService.board_like(boardDto);
            return ResponseEntity.ok(num);
        }catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류발생");
        }
    }



    @PostMapping("/password/verify")
    @ResponseBody
    public ResponseEntity<?> verifyPassword(@RequestBody BoardDto boardDto){
        try {
            log.info("dto={}",boardDto);
            String password = boardDto.getPassword();

            String dtoUsername = boardDto.getMemberDto().getUsername();

            Long boardId = boardDto.getId();
            Integer integer = boardService.passwordVerify(boardId, password, dtoUsername);

            if (integer==2){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("등록한 사용자만 수정할수 있습니다.");
            } else if (integer==1){
                return ResponseEntity.ok(integer);
            }
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("비밀번호가 일치하지 않습니다.");

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류발생");
        }
    }

    private static Member getSession(Model model, HttpSession session) {      //로그인한 사용자 가져오기
        Member loginMember = (Member) session.getAttribute("loginMember");
        model.addAttribute("loginMember",loginMember);
        return loginMember;
    }

}
