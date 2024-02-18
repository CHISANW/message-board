package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.AddressDto;
import messageboard.Dto.MemberDto;
import messageboard.entity.member.Member;
import messageboard.event.MemberJoinEvent;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private final MemberServiceImpl memberService;
    private final ApplicationEventPublisher eventPublisher;


    @GetMapping("/createMember")
    public String createMember(Model model){
        model.addAttribute("member",new MemberDto());
        return "member/joinMember";
    }

    @PostMapping("/createMember")
    public String createMemberPost(@Valid @ModelAttribute("member")MemberDto memberDto, BindingResult result,Model model){
        boolean b = memberService.VerificationOfSingUp(memberDto);
        log.info("b={}",b);

        if (!b){
            ObjectError error = new ObjectError("globalError", "아이디및 중복검사를 해주세요");
            result.addError(error);
        }

        if (result.hasErrors()){
            return "member/joinMember";
        }

        Member member = memberService.saveDto(memberDto);
        eventPublisher.publishEvent(new MemberJoinEvent(member));
        return "redirect:/";
    }




}
