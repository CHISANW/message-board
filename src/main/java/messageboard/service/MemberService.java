package messageboard.service;

import messageboard.Dto.MemberDto;
import messageboard.entity.member.Member;
import org.springframework.stereotype.Service;


public interface MemberService {

    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);

    Member findByLoginId(String loginId);
}
