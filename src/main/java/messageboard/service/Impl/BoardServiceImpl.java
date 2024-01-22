package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import messageboard.entity.Member;
import messageboard.repository.BoardRepository;
import messageboard.service.BoardService;
import messageboard.service.MemberService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberService memberService;


    @Override
    public Board save(BoardDto boardDto) {

        String username = boardDto.getMemberDto().getUsername();
        Member byUsername = memberService.findByUsername(username);
        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(byUsername.getUsername())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
                .count(0)
                .member(byUsername)
                .views(0)
                .build();

        Board save = boardRepository.save(build);
        return save;
    }

    @Override
    public Board findByBoardId(Long boardId) {
        try {
           return boardRepository.findById(boardId)
                   .orElseThrow(()->new RuntimeException("Board not found with ID: "+ boardId));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error while finding Board by ID", e);
        }
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
                boardRepository.deleteById(boardId);
                return true;
            }
        } else {
            throw new EntityNotFoundException("게시물이 존재하지 않습니다. ID: " + boardId);
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

}
