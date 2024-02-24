package messageboard.service;

public interface EmailService {

    default String generateVerification(String username)       //이메일 인증정보 생성
    {
        return null;
    }

    default String getVerificationIdByUsername(String username)    //이메일 인증 Id 생성
    {
        return null;
    }

    default public String getUsernameForVerificationId(String username)    //이메일 인증 ID 대흥하는 ID 생성
    {
        return null;
    }


}
