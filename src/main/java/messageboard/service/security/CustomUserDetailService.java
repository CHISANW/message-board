package messageboard.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.member.Member;
import messageboard.repository.MemberRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username){
            Member byLoginId = memberRepository.findByCaseSensitiveLoginId(username);
            return User.builder()
                    .username(byLoginId.getLoginId())
                    .password(byLoginId.getPassword())
                    .disabled(!byLoginId.isVerified())
                    .authorities("ROLE_USER").build();

    }
}
