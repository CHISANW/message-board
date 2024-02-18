package messageboard.controller;

import lombok.RequiredArgsConstructor;
import messageboard.Dto.MemberDto;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class SingUpController {


    private final MemberServiceImpl singUpService;

    @PostMapping("/check/loginId")
    public ResponseEntity<?> checkLoginId(@RequestBody MemberDto memberDto){
        try{
           return singUpService.LoginIdVaild(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류발생");
        }
    }

    @PostMapping("/duplicate/password")
    public ResponseEntity<Map<String,Boolean>> duplicatePassword(@RequestBody MemberDto memberDto){
        try{
            return singUpService.duplicatePassword(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류발생");
        }
    }


    @PostMapping("/Strength/password")
    public ResponseEntity<Map<String,Boolean>> passwordStrength(@RequestBody MemberDto memberDto){
        try{
            return singUpService.checkPasswordStrength(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }
}
