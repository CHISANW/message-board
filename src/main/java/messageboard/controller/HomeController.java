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

        Member loginMember = (Member) session.getAttribute("loginMember");

        model.addAttribute("loginMember",loginMember);
        return "home";
    }
}
