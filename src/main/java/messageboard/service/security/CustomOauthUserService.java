package messageboard.service.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.member.Member;
import messageboard.repository.MemberRepository;
import messageboard.service.MemberService;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession session;
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        String tokenValue = userRequest.getAccessToken().getTokenValue();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String requestId = userRequest.getClientRegistration().getRegistrationId();
        session.setAttribute("Token",tokenValue);

        log.info("tokenValue={}",tokenValue);
        log.info("oAuth2User={}",oAuth2User);
        log.info("oAuth2User={}",oAuth2User.getAttributes());
        log.info("oAuth2User={}",oAuth2User.getAttributes().get("id"));
        log.info("userRequestget.ClientRegistration={}",userRequest.getClientRegistration());
        log.info("requestId={}",requestId);


        Oauth2UserInfo oauth2UserInfo = null;
        if (requestId.equals("google")){
            oauth2UserInfo= new GoogleUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),oauth2UserInfo.getEmail(),requestId);

        }if (requestId.equals("facebook")){
            oauth2UserInfo=new FacebookUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),oauth2UserInfo.getEmail(),requestId);
        }

        return super.loadUser(userRequest);
    }

    private void save(String id,String name,String email,String requestId){
        Member member = Member.builder()
                .loginId(id)
                .username(name)
                .email(email)
                .loginType(requestId)
                .build();

        Member byLoginId = memberRepository.findByLoginId(id);
        if (byLoginId==null)
            memberRepository.save(member);
    }
}
