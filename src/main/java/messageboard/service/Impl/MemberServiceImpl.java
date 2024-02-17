package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.LoginDto;
import messageboard.Dto.MemberDto;
import messageboard.Exception.BadRequestException;
import messageboard.entity.Member;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Member saveEntity(Member member) {
        return memberRepository.save(member);
    }

    @Override
    public Member saveDto(MemberDto memberDto) {
        Member member = Member.builder()
                .username(memberDto.getUsername())
                .loginId(memberDto.getLoginId())
                .password(passwordEncoder.encode(memberDto.getPassword()))
                .build();
        return saveEntity(member);
    }

    @Override
    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username);
    }

    @Override
    public Member findByLoginId(String loginId) {
        Member byLoginId = memberRepository.findByLoginId(loginId);
        if (byLoginId==null){
            throw new BadRequestException("Not found User= "+loginId);
        }

        return byLoginId;
    }


//    public boolean login(LoginDto loginDto){                //로그인 구현
//        String username = loginDto.getUsername();
//        String password = loginDto.getPassword();
//        Member byUsername = memberRepository.findByUsername(username);
//        if(byUsername!=null) {
//            if (byUsername.getPassword().equals(password)) {
//                return true;
//            }
//        }
//        return false;
//    }
}
