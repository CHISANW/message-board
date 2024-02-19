package messageboard.controller;

import lombok.RequiredArgsConstructor;
import messageboard.Dto.MemberDto;
import messageboard.service.Impl.MemberServiceImpl;
import messageboard.service.MemberService;
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
            Map<String, Boolean> stringBooleanMap = singUpService.LoginIdValid(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류발생");
        }
    }

    @PostMapping("/check/password/duplicate")
    public ResponseEntity<Map<String,Boolean>> duplicatePassword(@RequestBody MemberDto memberDto){
        try{
            Map<String, Boolean> stringBooleanMap = singUpService.duplicatePassword(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류발생");
        }
    }


    @PostMapping("/check/password/strength")
    public ResponseEntity<Map<String,Boolean>> passwordStrength(@RequestBody MemberDto memberDto){
        try{
            Map<String, Boolean> stringBooleanMap = singUpService.checkPasswordStrength(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }


    @PostMapping("/check/username/valid")
    public ResponseEntity<Map<String,Boolean>> UserNameValid(@RequestBody MemberDto memberDto){
        try{
            Map<String, Boolean> stringBooleanMap = singUpService.userNameValid(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }

    @PostMapping("/check/phoneNumber/valid")
    public ResponseEntity<Map<String,Boolean>> PhoneNumberValid(@RequestBody MemberDto memberDto){
        try{
            Map<String, Boolean> stringBooleanMap = singUpService.phoneNumberValid(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }

    @PostMapping("/check/email/valid")
    public ResponseEntity<Map<String,Boolean>> EmailValid(@RequestBody MemberDto memberDto){
        try{
            Map<String, Boolean> stringBooleanMap = singUpService.emailValid(memberDto);
            return ResponseEntity.ok(stringBooleanMap);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("오류발생");
        }
    }

}
