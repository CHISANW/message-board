package messageboard.repository;

import messageboard.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findAll(Pageable pageable);
    Page<Board> findByTitleContaining(String title,Pageable pageable);

    Page<Board> findAllByOrderByCountDesc(Pageable pageable);   //조회수를 내림차순 정렬

    Page<Board> findAllByOrderByViewsDesc(Pageable pageable);  // 조회수 순으로 정렬

    Page<Board> findAllByOrderByDateTimeDesc(Pageable pageable);

    Page<Board> findAllByOrderByBoardLikeDesc(Pageable pageable);
}

