package messageboard.service;

public interface EmailService {

    String generateVerification(String username);       //이메일 인증정보 생성

    String getVerificationIdByUsername(String username);    //이메일 인증 Id 생성

    String getUsernameForVerificationId(String username) ;   //이메일 인증 ID 대흥하는 ID 생성


}
