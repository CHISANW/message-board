package messageboard.repository;

import messageboard.entity.Board_Like_check;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLIkeRepository extends JpaRepository<Board_Like_check,Long> {

    @Query("select b from  Board_Like_check b where b.member.id = :memberId and b.board.Id=:boardId")
    Board_Like_check findMemberId(@Param("memberId")Long memberId, @Param("boardId") Long boardId);

    @Modifying
    @Query("delete from Board_Like_check b where b.board.Id=:boardId")
    void deleteBoard_Id(@Param("boardId") Long boardId);
}
