package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.MemberDto;
import messageboard.entity.Member;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;

    @Override
    public Member save(MemberDto memberDto) {
        try {
            Member member = Member.builder()
                    .username(memberDto.getUsername()).build();

            Member save = memberRepository.save(member);
            return save;

        }catch (Exception e){
           e.printStackTrace();
           throw new RuntimeException();

        }

    }
}
