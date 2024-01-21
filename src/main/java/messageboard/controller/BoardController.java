package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import messageboard.service.BoardService;
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
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class BoardController {

    private final BoardService boardService;

    @GetMapping("/board")
    public String board(Model model, @PageableDefault(size = 4) Pageable pageable){
        Page<Board> pageAll = boardService.findPageAll(pageable);
        List<Board> boardAll = pageAll.getContent();

        int currentPage = pageAll.getPageable().getPageNumber() + 1; // 현재 페이지 (0부터 시작하는 인덱스를 1로 변환)
        int totalPages = pageAll.getTotalPages(); // 전체 페이지 수

        int visiblePages = 3;

        int startPage = Math.max(1, currentPage - visiblePages / 2); // 현재 페이지를 중심으로 앞으로 visiblePages/2 만큼의 범위를 표시
        int endPage = Math.min(totalPages, startPage + visiblePages - 1); // 시작 페이지부터 visiblePages 개수만큼의 범위를 표시


        if(boardAll !=null) {
            model.addAttribute("boardAll",boardAll);
        }
        model.addAttribute("startPage",startPage);
        model.addAttribute("endPage",endPage);

        model.addAttribute("page",pageAll);
        return "board/board";
    }

    @GetMapping("/boardWrit")
    public String write(Model model){

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
            Board save = boardService.save(boardDto);
            log.info("save={}",save);

            return ResponseEntity.ok("성공");

        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("오류발생");
        }
    }

    @GetMapping("/board/{boardId}")
    public String boardInfo(@PathVariable(name = "boardId") Long boardId,Model model){
        Board byBoardId = boardService.findByBoardId(boardId);
        model.addAttribute("board",byBoardId);
        return "board/boardInfo";
    }

    @DeleteMapping("/delete")
    @ResponseBody
    public ResponseEntity<?> boardDelete(@RequestBody BoardDto boardDto){
        try{
            Long id = boardDto.getId();
            boardService.deleteBoard(id);
            return ResponseEntity.ok("삭제성공");
        }catch (EntityNotFoundException e){
            e.printStackTrace();
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("게시물이 존재하지 않습니다.");
        }
    }

    @GetMapping("/board/update/{id}")
    public String updateGetBoard(@PathVariable(name = "id") Long id,Model model){
        Board byBoardId = boardService.findByBoardId(id);

        model.addAttribute("board",byBoardId);
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

}
