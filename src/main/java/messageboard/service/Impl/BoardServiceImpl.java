package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import messageboard.repository.BoardRepository;
import messageboard.service.BoardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;


    @Override
    public Board save(BoardDto boardDto) {

        Board build = Board.builder()
                .title(boardDto.getTitle())
                .dateTime(LocalDateTime.now())
                .writer(boardDto.getWriter())
                .password(boardDto.getPassword())
                .content(boardDto.getContent())
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

    @Override
    public void deleteBoard(Long boardId) {
        Optional<Board> boardOptional = boardRepository.findById(boardId);
        if (boardOptional.isPresent()) {
            boardRepository.deleteById(boardId);
        } else {
            throw new EntityNotFoundException("게시물이 존재하지 않습니다. ID: " + boardId);
        }

    }

    @Override
    public Board updateBoard(BoardDto boardDto) {

        Board byBoardId = findByBoardId(boardDto.getId());
        byBoardId.update(boardDto.getTitle(), boardDto.getContent());
        return boardRepository.save(byBoardId);

    }


}
