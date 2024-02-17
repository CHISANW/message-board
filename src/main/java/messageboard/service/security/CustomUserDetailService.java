package messageboard.service.security;

import lombok.RequiredArgsConstructor;
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
            Member byLoginId = memberRepository.findByLoginId(username);

            return User.builder()
                    .username(byLoginId.getLoginId())
                    .password(byLoginId.getPassword())
                    .disabled(!byLoginId.isVerified())
                    .authorities("ROLE_USER").build();

    }
}
