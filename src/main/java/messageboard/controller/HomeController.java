package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.member.Member;
import messageboard.service.MemberService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberService memberService;
    @GetMapping("/")
    public String home(Model model){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof User){
            User user = (User) authentication.getPrincipal();
            log.info("지금은 일반 사용자 로그인 입니다..");
            Member member = memberService.findByLoginId(user.getUsername());
            model.addAttribute("member",member);
        }
//        if (a)OAuth2User

        if (authentication.getPrincipal() instanceof OAuth2User) {
            String name = authentication.getName();
            log.info("지금은 OauthUser 로그인 입니다.");
            Member member = memberService.findByUsername(name);
            model.addAttribute("member",member);
        }

        return "home";
    }
}
