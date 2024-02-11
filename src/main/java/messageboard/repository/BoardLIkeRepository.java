package messageboard.repository;

import messageboard.entity.Board_Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface BoardLIkeRepository extends JpaRepository<Board_Like,Long> {

    @Query("select b from  Board_Like b where b.member.id = :memberId")
    Board_Like findMemberId(@Param("memberId")Long memberId);

    @Modifying
    @Query("delete from Board_Like b where b.member.id=:memberId")
    void deleteMemberId(@Param("memberId")Long memberId);


    @Modifying
    @Query("delete from Board_Like b where b.board.Id=:boardId")
    void deleteBoard_Id(@Param("boardId") Long boardId);
}
