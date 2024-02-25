package messageboard.service;

import messageboard.Dto.MemberDto;
import messageboard.entity.member.Member;
import org.springframework.stereotype.Service;


public interface MemberService {

    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);

    Member findByLoginId(String loginId);

    Member findByUsernameAndLoginId(String username,String loginId);

    Boolean findPasswordByEmailAndIdAndName(String username,String email, String loginId);

//    Boolean findByCaseSensitiveEmail(String email);


}
