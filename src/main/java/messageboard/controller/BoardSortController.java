package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.repository.BoardRepository;
import messageboard.service.Impl.BoardServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardSortController {

    private final BoardServiceImpl boardService;

    private final BoardRepository boardRepository;



    @GetMapping("/board/sortType")
    public String likeSon(@RequestParam(value = "sortBoard",defaultValue = "",required = false) String sortType,
                          @RequestParam(value = "title",defaultValue = "", required = false) String title,
                          Model model,
                          HttpSession session,
                          @PageableDefault(size = 10) Pageable pageable){

        Member loginMember = (Member) session.getAttribute("loginMember");

        Page<Board> boardSearch = boardService.search(title, pageable);
        List<Board> boardAll = boardSearch.getContent();

        log.info("boardAll={]",boardAll);
        log.info("boardSearch={]",boardSearch);

        if (sortType.equals("likeType")){
            Page<Board> boards = boardService.likeSortDesc(pageable);
            List<Board> content = boards.getContent();
            int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
            int totalPages = boards.getTotalPages(); // 전체 페이지 수

            String son="좋아요순";
            int visiblePages = 3;

            int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
            int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
            model.addAttribute("sortBoardList",content);
            model.addAttribute("page",boards);
            model.addAttribute("son",son);

        }else if (sortType.equals("lastBoardType")){
            Page<Board> boards = boardService.lasBoardSortDesc(pageable);
            List<Board> content = boards.getContent();
            int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
            int totalPages = boards.getTotalPages(); // 전체 페이지 수
            String son="최신글순";
            int visiblePages = 3;

            int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
            int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
            model.addAttribute("sortBoardList",content);
            model.addAttribute("page",boards);
            model.addAttribute("son",son);
        }else if (sortType.equals("manyCountType")){
            Page<Board> boards = boardService.CommentSOrtDesc(pageable);
            List<Board> content = boards.getContent();
            int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
            int totalPages = boards.getTotalPages(); // 전체 페이지 수
            String son="많은 댓글순";
            int visiblePages = 3;

            int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
            int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
            model.addAttribute("sortBoardList",content);
            model.addAttribute("page",boards);
            model.addAttribute("son",son);
        }else if (sortType.equals("manyViewType")){
            Page<Board> boards = boardService.viewSortDesc(pageable);
            List<Board> content = boards.getContent();
            int currentPage = boards.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
            int totalPages = boards.getTotalPages(); // 전체 페이지 수
            String son="조회수순";

            int visiblePages = 3;

            int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
            int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시

            model.addAttribute("startPage",startPage);
            model.addAttribute("endPage",endPage);
            model.addAttribute("sortBoardList",content);
            model.addAttribute("page",boards);
            model.addAttribute("son",son);
        }

        model.addAttribute("sortBoard",sortType);
        model.addAttribute("loginMember",loginMember);

        return "board/sortBoard";
    }

    @GetMapping("/board/test")
    @ResponseBody
    public ResponseEntity<?> likeSontest(@RequestParam(value = "sortA",required = false,defaultValue = "") String type,
                                         @PageableDefault(size = 10) Pageable pageable) {
      try{
          Map<Long,Board> boardMap = new LinkedHashMap<>();

          if(type.equals("like")){
              Page<Board> allByOrderByBoardLike = boardRepository.findAllByOrderByCountDesc(pageable);
              List<Board> content = allByOrderByBoardLike.getContent();

              allByOrderByBoardLike.getTotalElements();
              for (Board board : content) {
                  boardMap.put(board.getId(),board);
              }
              return ResponseEntity.ok(boardMap);
          }
          return null;
      }catch (Exception e){
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류");
      }

    }



}
