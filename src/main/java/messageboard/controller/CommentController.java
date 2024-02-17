package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.Exception.*;
import messageboard.entity.Comment;
import messageboard.service.CommentService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @DeleteMapping("/delete/comment")
    public ResponseEntity<?> deleteComment(@RequestBody CommentDto commentDto){
        try {
            commentService.deleteComment(commentDto);
            return ResponseEntity.ok("삭제 성공");
        }catch (CommentException e){
            e.printStackTrace();
            throw new BadRequestException("댓글 작성자만이 삭제가능 합니다.");
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
        }catch (Login_RestException e) {
           throw new Login_RestException("로그인을 하지 않았습니다.");
        }catch (NotFindPageException e){
            throw new NotFindPage_RestException("게시글이 존재하지 않습니다.");
        }
    }

    @PostMapping("/board/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentDto commentDto){
        try {
            commentService.updateComment(commentDto);
            return ResponseEntity.ok("게시물 수정 성공");
        }catch (CommentException e){
            throw new CommentException("해당 댓글이 존재하지 않습니다.");
        }catch (Login_RestException e){
            e.printStackTrace();
            throw new Login_RestException("작성자만 수정 할수있습니다.");
        }
    }

}
