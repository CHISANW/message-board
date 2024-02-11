package messageboard.repository;

import messageboard.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {


    @Query("select c from Comment c where c.board.Id = :id")
    List<Comment> findByBoard_Id(@Param("id") Long id);

    @Query("SELECT COUNT(c) from Comment c where c.board.Id=:id")
    Integer countComment(@Param("id") Long boardId);

    @Query("SELECT c.board.Id from Comment c where c.id =:commentId")
    Long findBoardId(@Param("commentId")Long commentId);

    @Modifying
    @Query("delete from Comment c where c.board.Id=:boardId")
    void deleteByBoardId(@Param("boardId") Long boardId);
}
