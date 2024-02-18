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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
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
    public String createMemberPost(@Valid @ModelAttribute("member")MemberDto memberDto, BindingResult result){
        if (result.hasErrors()){
            log.info("a={}",result.getFieldErrors().get(0).getDefaultMessage());
            return "member/joinMember";
        }

        Member member = memberService.saveDto(memberDto);
        eventPublisher.publishEvent(new MemberJoinEvent(member));
        return "redirect:/?validate";
    }


}
