package messageboard.service.security;

import lombok.RequiredArgsConstructor;
import messageboard.Exception.Login_RestException;
import messageboard.entity.Member;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;
    @Override
    public UserDetails loadUserByUsername(String username){
            Member byLoginId = memberRepository.findByLoginId(username);

            return User.builder()
                    .username(byLoginId.getLoginId())
                    .password(byLoginId.getPassword())
                    .authorities("ROLE_USER").build();

    }
}
