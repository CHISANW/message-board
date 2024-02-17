package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
public class LoginController {

    @GetMapping("/login")
    public String login(HttpServletRequest request) {
        String referer = request.getHeader("referer");
        request.getSession().setAttribute("referer",referer);
        return "member/login";
    }

    @GetMapping("/login-emailVerified")
    public String loginVerified(Model model){
        model.addAttribute("loginVerified",true);
        return "member/login";
    }

    @GetMapping("/login-disabled")
    public String loginDisabled(Model model) {
        model.addAttribute("loginDisabled", true);
        return "member/login";
    }

    @GetMapping("/login-error")
    public String loginError(Model model) {
        model.addAttribute("loginError", true);
        return "member/login";
    }
}
