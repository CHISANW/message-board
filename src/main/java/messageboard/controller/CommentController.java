package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.Exception.CommentException;
import messageboard.entity.Comment;
import messageboard.entity.Member;
import messageboard.service.CommentService;
import messageboard.service.Impl.CommentServiceImpl;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.websocket.Session;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;
    private final MemberServiceImpl memberService;

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
    public ResponseEntity<?> comment(@RequestBody CommentDto commentDto, HttpSession session){
        try{
            Comment saveComment = commentService.save(commentDto);
            return ResponseEntity.ok(saveComment);
        }catch (IllegalStateException e) {
           throw new CommentException("로그인을 하지 않았습니다.");
        }
    }

}
