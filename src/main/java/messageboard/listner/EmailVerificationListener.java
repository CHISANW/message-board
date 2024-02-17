package messageboard.listner;

import lombok.RequiredArgsConstructor;
import messageboard.entity.member.Member;
import messageboard.event.MemberJoinEvent;
import messageboard.service.EmailService;
import org.springframework.context.ApplicationListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
public class EmailVerificationListener implements ApplicationListener<MemberJoinEvent> {

    private final EmailService emailService;
    private final JavaMailSender mailSender;

    @Override
    public void onApplicationEvent(MemberJoinEvent event) {
        Member member = event.getMember();
        String loginId = member.getLoginId();
        String verification = emailService.generateVerification(loginId);
        String email = event.getMember().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("자유 게시판 가입 인증 안내입니다.");
        message.setText(getText(member,verification));
        message.setFrom("wlwhsrjaeka@naver.com");
        message.setTo(email);
        mailSender.send(message);
    }

    private String getText(Member member, String verificationId) {
        String encodedId = new String(Base64.getEncoder().encode(verificationId.getBytes()));
        StringBuffer buffer = new StringBuffer();
        buffer.append("받는이 ").append(member.getUsername()).append(" ").append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("회원 가입을 진행하기 위해서 아래절차를 따라해주세요. ");

        buffer.append("링크에 접속해 회원가입 인증을 진행해주시기 바랍니다.: ");
        buffer.append("http://localhost:8080/verify/email?id=").append(encodedId);
        buffer.append(System.lineSeparator()).append(System.lineSeparator());
        buffer.append("Regards,").append(System.lineSeparator()).append("자유게시판");
        return buffer.toString();
    }
}
