package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.entity.member.Member;
import messageboard.service.EmailService;
import messageboard.service.FindEmailService;
import messageboard.service.MemberService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Base64;

@Controller
@RequiredArgsConstructor
@Slf4j
public class EmailVerificationController {

    private final EmailService emailService;
    private final FindEmailService findEmailService;
    private final MemberService memberService;

    @GetMapping("/verify/email")
    public String verifyEmail(@RequestParam String id){
        byte[] actualId = Base64.getDecoder().decode(id.getBytes());
        String username = emailService.getUsernameForVerificationId(new String(actualId));
        if(username!=null){
            Member user = memberService.findByLoginId(username);
            user.setVerified(true);
            memberService.saveEntity(user);
            return "redirect:/login-emailVerified";
        }
        return "redirect:/login-error";
    }

    @PostMapping("/findId/mailConfirm")
    @ResponseBody
    public String mailConfirm(@RequestParam("email") String email)throws Exception{
        log.info("email={}",email);
        String code = findEmailService.sendSimpleMessage(email);
        System.out.println("인증코드 : "+code);
        return code;
    }

}
