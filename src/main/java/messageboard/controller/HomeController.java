package messageboard.controller;

import lombok.extern.slf4j.Slf4j;
import messageboard.entity.Member;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

@Controller
@Slf4j
public class HomeController {

    @GetMapping("/")
    public String home(Model model,HttpSession session){

        Member member = (Member) session.getAttribute("member");
        log.info("member={}",member);
        if (member != null) {
            model.addAttribute("loginMember", member);
        }
        return "home";
    }
}
