package messageboard.service;

import messageboard.Dto.MemberDto;
import messageboard.entity.Member;

public interface MemberService {

    Member saveEntity(Member member);

    Member saveDto(MemberDto memberDto);

    Member findByUsername(String username);
}
