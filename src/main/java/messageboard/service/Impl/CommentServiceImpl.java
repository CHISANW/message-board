package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.entity.Board;
import messageboard.entity.Comment;
import messageboard.entity.Member;
import messageboard.repository.CommentRepository;
import messageboard.service.CommentService;
import messageboard.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardServiceImpl boardService;
    private final MemberService memberService;

    @Override
    public Comment save(CommentDto commentDto) {

        Long id = commentDto.getBoardDto().getId();
        String username = commentDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);

        Board byBoardId = boardService.findByBoardId(id);

        Comment build = Comment.builder()
                .content(commentDto.getComment())
                .dateTime(LocalDateTime.now())
                .member(byUsername)
                .board(byBoardId).build();


        Comment save = commentRepository.save(build);
        setComment(byBoardId.getId());
        return save;
    }


    @Override    //댓글 모두 보여주기
    public List<Comment> findAllComment(Long boardId) {
        List<Comment> byBoardId = commentRepository.findByBoard_Id(boardId);
        return byBoardId;
    }

    @Override     //댓글지우기
    public void deleteComment(Long commentId) {
        try {
            Long boardId = commentRepository.findBoardId(commentId);
            commentRepository.deleteById(commentId);
            setComment(boardId);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override //댓글수 카운트
    public Integer countComment(Long boardId) {
        try{
            return commentRepository.countComment(boardId);
        }catch (Exception e){
            e.printStackTrace();
            throw new RuntimeException("값이 나오지 않았습니다.");
        }
    }

    private Board setComment(Long boardId){       // 총합 댓글 갯수 설정
        Integer countComment = countComment(boardId);
        Board byBoardId = boardService.findByBoardId(boardId);
        byBoardId.setCount(countComment);
        return boardService.entitySave(byBoardId);
    }


}
