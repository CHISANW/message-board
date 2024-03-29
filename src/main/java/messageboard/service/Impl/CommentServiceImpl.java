package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.CommentDto;
import messageboard.Exception.CommentException;
import messageboard.Exception.Login_RestException;
import messageboard.entity.Board;
import messageboard.entity.Comment;
import messageboard.entity.member.Member;
import messageboard.repository.CommentRepository;
import messageboard.service.CommentService;
import messageboard.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

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

        Long boardId = commentDto.getBoardDto().getId();
        String username = commentDto.getMemberDto().getUsername();
        String loginId = commentDto.getMemberDto().getLoginId();
        Member byUsername = memberService.findByUsernameAndLoginId(username,loginId);

        Board byBoardId = boardService.findByBoardId(boardId);

        if (byUsername==null){
            throw new Login_RestException();
        }


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
    public void deleteComment(CommentDto commentDto){
        String dtoUsername = commentDto.getMemberDto().getUsername();
        String loginId = commentDto.getMemberDto().getLoginId();
        String username = memberService.findByUsernameAndLoginId(dtoUsername,loginId).getUsername();  //로그인한 사용자 이름
        Long boardId = commentDto.getBoardDto().getId();


        Long commentDtoId = commentDto.getId();
        Optional<Comment> byId = commentRepository.findById(commentDtoId);
        if (byId.isPresent()){
            Long commentId = byId.get().getId();
            String commentWriter = byId.get().getMember().getUsername();
            Board byBoardId = boardService.findByBoardId(boardId);
            byBoardId.setCount(byBoardId.getCount()-1);
            boardService.entitySave(byBoardId);

            if (username != commentWriter){
                throw new CommentException("댓글 지우기 로직 오류");
            }
            commentRepository.deleteById(commentId);
        }

    }

    @Override
    public void deleteBoardID(Long boardId) {
        commentRepository.deleteByBoardId(boardId);
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

    @Override
    public void updateComment(CommentDto commentDto) {
        Long id = commentDto.getId();
        String username = commentDto.getMemberDto().getUsername();
        String loginId = commentDto.getMemberDto().getLoginId();
        Member byUsername = memberService.findByUsernameAndLoginId(username,loginId);

        Comment comment = commentRepository.findById(id).orElseThrow(() -> new CommentException("댓글 수정시 Id 오류"));

        log.info("서브스 댓글 수정={}",comment.getMember().getLoginId().equals(byUsername.getLoginId()));
        log.info("comment 댓글 수정={}",comment.getMember().getLoginId());
        log.info("byUsername 댓글 수정={}",byUsername.getLoginId());

        if(byUsername==null || !comment.getMember().getLoginId().equals(byUsername.getLoginId())){
            throw new Login_RestException("사용자 인증 오류");
        }

        comment.setContent(commentDto.getComment());
        comment.setUpdateDateTime(LocalDateTime.now());
        commentRepository.save(comment);

    }

    private Board setComment(Long boardId){       // 총합 댓글 갯수 설정
        Integer countComment = countComment(boardId);
        Board byBoardId = boardService.findByBoardId(boardId);
        byBoardId.setCount(countComment);
        return boardService.entitySave(byBoardId);
    }


}
