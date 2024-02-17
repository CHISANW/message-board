package messageboard.repository;

import messageboard.entity.member.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmailRepository extends JpaRepository<EmailVerification, String> {

    EmailVerification findByUsername(String username);
    boolean existsByUsername(String username);              //레코드 존재여부 확인
}
