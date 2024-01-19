package messageboard.service;

import messageboard.Dto.BoardDto;
import messageboard.entity.Board;

public interface BoardService {

    Board save(BoardDto boardDto);
}
