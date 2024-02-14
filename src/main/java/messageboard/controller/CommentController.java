package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.Exception.CommentException;
import messageboard.Exception.Login_RestException;
import messageboard.entity.Comment;
import messageboard.entity.Member;
import messageboard.service.Impl.CommentServiceImpl;

import messageboard.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;


@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;
    private final MemberService memberService;

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

    @PostMapping("/board/update")
    public ResponseEntity<?> updateComment(@RequestBody CommentDto commentDto){
        try {
            String username = commentDto.getMemberDto().getUsername();
            Member byUsername = memberService.findByUsername(username);
            log.info("aaa={}",commentDto.getId());
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
