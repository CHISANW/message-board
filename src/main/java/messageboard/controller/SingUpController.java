package messageboard.controller;

import lombok.RequiredArgsConstructor;
import messageboard.Dto.MemberDto;
import messageboard.service.Impl.MemberServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerErrorException;

@RestController
@RequiredArgsConstructor
public class SingUpController {


    private final MemberServiceImpl singUpService;

    @PostMapping("/duplicate/loginId")
    public ResponseEntity<?> duplicateLoginId(@RequestBody MemberDto memberDto){
        try{
           return singUpService.duplicateLoginId(memberDto);
        }catch (Exception e){
            e.printStackTrace();
            throw new ServerErrorException("서버오류발생");
        }
    }
}
