package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Exception.Login_RestException;
import messageboard.Exception.NotFindPageException;
import messageboard.entity.Board;
import messageboard.entity.Board_Like_check;
import messageboard.entity.Member;
import messageboard.repository.BoardLIkeRepository;
import messageboard.repository.BoardRepository;
import messageboard.repository.CommentRepository;
import messageboard.service.BoardService;
import messageboard.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
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

        String username = boardDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);
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
     * @param boardId
     * @param memberUsername
     * @return
     */
    @Override
    public boolean deleteBoard(Long boardId,String memberUsername) {
        Member byUsername = memberService.findByUsername(memberUsername);
        Optional<Board> boardOptional = boardRepository.findById(boardId);

        if (boardOptional.isPresent()) {
            String writer = boardOptional.get().getWriter();
            if (byUsername!=null && writer.equals(memberUsername))
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

        byBoardId.update(boardDto.getTitle(), boardDto.getContent());
        return boardRepository.save(byBoardId);

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

        Board board = findByBoardId(boardDtoId);
        Member member = memberService.findByUsername(username);         //123

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

    @Override
    public Page<Board> likeSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByBoardLikeDesc(pageable);
    }

    @Override
    public Page<Board> viewSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByViewsDesc(pageable);
    }

    @Override
    public Page<Board> CommentSOrtDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByCountDesc(pageable);
    }

    @Override
    public Page<Board> lasBoardSortDesc(Pageable pageable) {
        return boardRepository.findAllByOrderByDateTimeDesc(pageable);
    }

    @Override
    public List<Long> likeSon() {
        return boardRepository.likeSon();
    }

    @Override
    public List<Long> manyViewsDesc() {
        return boardRepository.manyViewDesc();
    }

    @Override
    public List<Long> manyCommentDesc() {
        return boardRepository.manyCommentDesc();
    }

    @Override
    public List<Long> lastBoardDesc() {
        return boardRepository.lastBoard();
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

    public Integer passwordVerify(Long boardId, String password,String username){
        Board byBoardId = findByBoardId(boardId);
        String boardPassword = byBoardId.getPassword();
        Member memberUsername = memberService.findByUsername(username);

        if (boardPassword.equals(password)) {
            if (memberUsername == null||!(byBoardId.getWriter().equals(username))) {
                return 2;  // 비밀번호는 o ,사용자 인증은 x
            }
            return 1;   //성공시 1로반환
        }
        return 0;
    }

    public List<Board> getSortedBoardsByType(String sortType) {
        List<Long> sortList = getSortList(sortType);
        List<Board> sortedBoards = new ArrayList<>();
        for (Long boardId : sortList) {
            Board board = findByBoardId(boardId);
            sortedBoards.add(board);
        }
        return sortedBoards;
    }

    public List<Long> getSortList(String sortType) {
        switch (sortType){
            case "likeType":
                return likeSon();
            case "manyViewType":
                return manyViewsDesc();
            case "manyCountType":
                return manyCommentDesc();
            case "lastBoardType":
                return  lastBoardDesc();
            default:
                throw new RuntimeException("잘못된 요청입니다.");
        }
    }
}
