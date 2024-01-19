package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.BoardDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.Member;
import messageboard.service.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberService memberService;



    @GetMapping("/addMember")
    public String addMember(Model model){
        model.addAttribute("member", new MemberDto());
        return "member/addMember";
    }

    @PostMapping("/addMember")
    public String SingUp(@ModelAttribute(name = "member") MemberDto memberDto, HttpSession session){
        Member save = memberService.save(memberDto);
        session.setAttribute("member",save);

        return "redirect:/";
    }




}
