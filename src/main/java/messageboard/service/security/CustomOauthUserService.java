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
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class CustomOauthUserService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;
    private final HttpSession session;

    /**
     * Oauth2 간편로그인시에 간편하게 회원가입을 따로하지않고 request 값을 통해  간편하게 회원가입을 진행한다.
     * @param userRequest the user request
     * @return 넘어온 request 값을 그대로반환
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {


        String tokenValue = userRequest.getAccessToken().getTokenValue();
        OAuth2User oAuth2User = super.loadUser(userRequest);
        String requestId = userRequest.getClientRegistration().getRegistrationId();
        session.setAttribute("Token",tokenValue);

        log.info("tokenValue={}",tokenValue);
        log.info("oAuth2User={}",oAuth2User);
        log.info("getAttributes={}",oAuth2User.getAttributes());
        log.info("properties={}",oAuth2User.getAttributes().get("properties"));
        log.info("email={}",oAuth2User.getAttributes().get("email"));
        log.info("userRequestget.ClientRegistration={}",userRequest.getClientRegistration());
        log.info("requestId={}",requestId);


        Oauth2UserInfo oauth2UserInfo = null;
        if (requestId.equals("google")){
            oauth2UserInfo= new GoogleUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),oauth2UserInfo.getEmail(),requestId);

        }else if (requestId.equals("facebook")){
            oauth2UserInfo=new FacebookUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),oauth2UserInfo.getEmail(),requestId);
        }else if (requestId.equals("kakao")) {
            oauth2UserInfo=new KakaoUserInfo(oAuth2User.getAttributes());
            save(oauth2UserInfo.getId(),oauth2UserInfo.getName(),oauth2UserInfo.getEmail(),requestId);
        }

        return super.loadUser(userRequest);
    }

    /**
     * 간편 로그인시에 자동으로 아이디, 이름,이메일, 로그인한 플랫폼을 이용해 회원가입을 한다.
     * @param id    사용자 아이디
     * @param name  사용자 이름
     * @param email 사용자 이메일
     * @param requestId 로그인한 Oauth2 플랫폼
     */
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
