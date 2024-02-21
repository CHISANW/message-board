package messageboard.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import messageboard.Dto.MemberDto;
import messageboard.Exception.BadRequestException;
import messageboard.entity.member.Member;
import messageboard.event.MemberJoinEvent;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ServerErrorException;

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
    @ResponseBody
    public ResponseEntity<?> createMemberPost(@Valid @RequestBody MemberDto memberDto, BindingResult result){
        try {
            boolean b = memberService.ValidationOfSignUp(memberDto);
            log.info("b={}",b);
            if (!b) {
                ObjectError error = new ObjectError("globalError", "회원가입시 모든 조건을 만족해주세요");
                result.addError(error);
            } else if (b) {

            }
            if (result.hasErrors()) {
                Map<String,String> errorMessage=new HashMap<>();
                for (FieldError error : result.getFieldErrors()) {
                    errorMessage.put(error.getField(),error.getDefaultMessage());
                }
                ObjectError globalError = result.getGlobalError();
                errorMessage.put(globalError.getObjectName(), globalError.getDefaultMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
            }

            Member member = memberService.saveDto(memberDto);
            eventPublisher.publishEvent(new MemberJoinEvent(member));
            return ResponseEntity.ok(member);
       } catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }




}
