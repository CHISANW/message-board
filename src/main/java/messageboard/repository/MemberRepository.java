package messageboard.repository;

import messageboard.entity.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberRepository extends JpaRepository<Member,Long> {

    Member findByUsername(String username);

    Member findByUsernameAndLoginId(String username,String LoginId);

    Member findByLoginId(String loginId);

    List<Member> findByEmail(String email);
    
    @Query("select m from Member m where  binary(m.loginId)=:loginId")      //MySQL의 BINARY 타입을 사용해 대소문자를 구문하기 위한 쿼리 사용
    Member findByCaseSensitiveLoginId(@Param("loginId") String loginId);

    List<Member> findByUsernameAndEmail(String username, String email);

    Member findByUsernameAndEmailAndLoginId(String username,String email,String loginId);

//    @Query("select m from Member m where  binary(m.email)=:email")      //MySQL의 BINARY 타입을 사용해 대소문자를 구문하기 위한 쿼리 사용
//    Member findByCaseSensitiveEmail(@Param("email") String email);
}
