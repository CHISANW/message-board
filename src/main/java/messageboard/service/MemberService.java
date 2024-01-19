package messageboard.service;


import messageboard.Dto.MemberDto;
import messageboard.entity.Member;

public interface MemberService {

    Member save(MemberDto memberDto);
}
