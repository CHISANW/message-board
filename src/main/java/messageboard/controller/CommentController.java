package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.entity.Comment;
import messageboard.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @DeleteMapping("/delete/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto){
        try {
            Long id = commentDto.getId();
            commentService.deleteComment(id);
            return ResponseEntity.ok("삭제 성공");
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버오류");
        }
    }

    @PostMapping("/board/comment")
    @ResponseBody
    public ResponseEntity<?> comment(@RequestBody CommentDto commentDto){
        try{
            Comment saveComment = commentService.save(commentDto);
            return ResponseEntity.ok(saveComment);
        }catch (Exception e){
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("댓글 작성 오류 발생");
        }
    }

//    @GetMapping("/countComment")
//    public ResponseEntity<?> countComment(@RequestParam Long id) {
//        try {
//            Integer countComment = commentService.countComment(id);
//            return ResponseEntity.ok(countComment);
//        } catch (NumberFormatException e) {
//            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Invalid comment ID format"));
//        } catch (Exception e) {
//            e.printStackTrace();
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Collections.singletonMap("error", "조회 중 오류 발생"));
//        }
//    }
}
