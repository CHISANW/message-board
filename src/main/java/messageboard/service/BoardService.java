package messageboard.service;

import messageboard.Dto.BoardDto;
import messageboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface BoardService {

    Board save(BoardDto boardDto);

    Board findByBoardId(Long boardId);

    Page<Board> findPageAll(Pageable pageable);

    boolean deleteBoard(Long boardId,String memberUsername);

    Board updateBoard(BoardDto boardDto);

    Page<Board> search(String title,Pageable pageable);
}
