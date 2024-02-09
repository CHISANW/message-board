package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.Exception.CommentException;
import messageboard.Exception.Login_RestException;
import messageboard.entity.Comment;
import messageboard.entity.Member;
import messageboard.service.Impl.CommentServiceImpl;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @DeleteMapping("/delete/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto){
        try {
            commentService.deleteComment(commentDto);
            return ResponseEntity.ok("삭제 성공");
        }catch (CommentException e){
            e.printStackTrace();
            throw new CommentException("댓글 작성자만이 삭제가능 합니다.");
        }catch (Login_RestException e){
            throw new Login_RestException("로그인을 하지않음");
        }
    }

    @PostMapping("/board/comment")
    @ResponseBody
    public ResponseEntity<?> comment(@RequestBody CommentDto commentDto){
        try{
            Comment saveComment = commentService.save(commentDto);
            return ResponseEntity.ok(saveComment);
        }catch (IllegalStateException e) {
           throw new Login_RestException("로그인을 하지 않았습니다.");
        }
    }

}
