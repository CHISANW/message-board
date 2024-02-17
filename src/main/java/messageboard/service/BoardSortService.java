package messageboard.service;

import messageboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BoardSortService {

    Page<Board> TypeSort (String type, Pageable pageable);
    String TypeValue(String typeValue);

    Page<Board> likeSortDesc(Pageable pageable);
    Page<Board> viewSortDesc(Pageable pageable);
    Page<Board> CommentSOrtDesc(Pageable pageable);
    Page<Board> lasBoardSortDesc(Pageable pageable);
}
