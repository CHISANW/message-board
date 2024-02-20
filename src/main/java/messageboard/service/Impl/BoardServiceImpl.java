package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Exception.BoardException;
import messageboard.Exception.Login_RestException;
import messageboard.Exception.NotFindPageException;
import messageboard.entity.Board;
import messageboard.entity.Board_Like_check;
import messageboard.entity.member.Member;
import messageboard.repository.BoardLIkeRepository;
import messageboard.repository.BoardRepository;
import messageboard.repository.CommentRepository;
import messageboard.service.BoardService;
import messageboard.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {


    private final BoardRepository boardRepository;
    private final MemberService memberService;
    private final BoardLIkeRepository boardLIkeRepository;
    private final CommentRepository commentRepository;

    @Override
    public Board save(BoardDto boardDto) {

        String loginId = boardDto.getMemberDto().getLoginId();
        String username = boardDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsernameAndLoginId(username,loginId);
        if (byUsername==null)
            throw new Login_RestException("상품 저장 오류");

        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(byUsername.getUsername())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
                .count(0)
                .member(byUsername)
                .boardLike(0)
                .views(0)
                .build();

        Board save = boardRepository.save(build);
        return save;
    }

    @Override
    public Board findByBoardId(Long boardId) {
            return boardRepository.findById(boardId)
                    .orElseThrow(() -> new NotFindPageException("해당 게시물을 찾을수가 없습니다.: " + boardId));
    }

    @Override
    public Page<Board> findPageAll(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    /**
     * true 반환시 삭제 성공 false 반환시 등록된 사용자만 삭제가능
     * @param boardDto
     * @return
     */
    @Override
    public boolean deleteBoard(BoardDto boardDto) {
        String username = boardDto.getMemberDto().getUsername();
        String loginId = boardDto.getMemberDto().getLoginId();

        Long boardId = boardDto.getId();

        Member byUsername = memberService.findByUsernameAndLoginId(username,loginId);
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (boardOptional.isPresent()) {
            String boardLoginId = boardOptional.get().getMember().getLoginId();
            if (byUsername!=null && boardLoginId.equals(byUsername.getLoginId()))
            {
                boardLIkeRepository.deleteBoard_Id(boardId);
                commentRepository.deleteByBoardId(boardId);
                boardRepository.deleteById(boardId);
                return true;
            }
        } else {
            throw new NotFindPageException("게시물이 존재하지 않습니다. ID: " + boardId);
        }
        return false;
    }

    @Override
    public Board updateBoard(BoardDto boardDto) {

        Board byBoardId = findByBoardId(boardDto.getId());
        String loginId = boardDto.getMemberDto().getLoginId();
        String username = boardDto.getMemberDto().getUsername();
        Member byLoginId = memberService.findByUsernameAndLoginId(username,loginId);

        if (byBoardId.getMember().getLoginId().equals(byLoginId.getLoginId())){
            byBoardId.update(boardDto.getTitle(), boardDto.getContent());
            return boardRepository.save(byBoardId);
        }else 
            throw new Login_RestException("작성자가 아닐시 게시판 수정중 오류발생");

    }

    @Override
    public Page<Board> search(String title,Pageable pageable) {
        return boardRepository.findByTitleContaining(title,pageable);
    }

    /**
     * 댓글 좋아요 기능
     *
     */
    @Override
    public int board_like(BoardDto boardDto) {
        Long boardDtoId = boardDto.getId();
        String username = boardDto.getMemberDto().getUsername();       //로그인 사용자 정보
        String loginId = boardDto.getMemberDto().getLoginId();       //로그인 사용자 정보

        Board board = findByBoardId(boardDtoId);
        Member member = memberService.findByUsernameAndLoginId(username,loginId);         //123

        if (member!=null){
            Board_Like_check byMemberId = boardLIkeRepository.findMemberId(member.getId(),board.getId());     //로그인 사용자 정보를 사용해 좋아요했는지 찾는과정

            if (byMemberId!=null&&byMemberId.getBoard().getId().equals(board.getId())) {
                board.setBoardLike(board.getBoardLike()-1);

                boardRepository.save(board);
                boardLIkeRepository.deleteBoard_Id(board.getId());
                return -1;
            }else {
                board.setBoardLike(board.getBoardLike() + 1);       //좋아요 1회 증가
                Board_Like_check boardLike = Board_Like_check.builder()
                        .like_check(true)
                        .member(member)
                        .board(board).build();
                boardLIkeRepository.save(boardLike);
                return 1;
            }
        }else
            throw new Login_RestException("로그인을 한후에 이용할수 있습니다.");

    }


    //조회수 기능
    public Board countViews(Long boardId){
        Board byBoardId = findByBoardId(boardId);
        Integer views = byBoardId.getViews();
        byBoardId.setViews(++views);
        return boardRepository.save(byBoardId);
    }

    public Board entitySave(Board board){
        return boardRepository.save(board);
    }

    @Override
    public Integer passwordVerify(BoardDto boardDto){
        String password = boardDto.getPassword();
        String username = boardDto.getMemberDto().getUsername();
        String loginId = boardDto.getMemberDto().getLoginId();
        Long boardId = boardDto.getId();

        Board byBoardId = findByBoardId(boardId);
        String boardPassword = byBoardId.getPassword();
        Member memberUsername = memberService.findByUsernameAndLoginId(username,loginId);
        log.info("1={}",byBoardId.getMember().getLoginId());
        log.info("2={}",memberUsername.getLoginId());

        if (boardPassword.equals(password)) {
            if (!byBoardId.getMember().getLoginId().equals(memberUsername.getLoginId())) {
                return 2;  // 비밀번호는 o ,사용자 인증은 x
            }
            return 1;   //성공시 1로반환
        }
        return 0;
    }



}
