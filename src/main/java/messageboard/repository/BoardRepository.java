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


    @Query("SELECT b.Id from Board b order by b.board_like desc ")
    List<Long> likeSon();

    @Query("SELECT b.Id from Board b order by b.views desc ")
    List<Long> manyViewDesc();

    @Query("SELECT b.Id from Board b order by b.count desc ")
    List<Long> manyCommentDesc();

    @Query("SELECT b.Id from Board b order by b.dateTime desc ")
    List<Long> lastBoard();
}

