package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.service.FindEmailService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Random;

@Service
@RequiredArgsConstructor
@Transactional
public class FindEmailServiceImpl implements FindEmailService {

    private final JavaMailSender mailSender;
    private String code;

    @Override
    public SimpleMailMessage CreateMessage(String to) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();

            message.setSubject("인증번호 요청안내");
            message.setFrom("wlwhsrjaeka@naver.com");
            message.setText(getText());
            message.setTo(to);;
            return message;

        }catch (Exception e){
            throw new RuntimeException();
        }

    }

    @Override
    public String sendSimpleMessage(String to) {
        code=createKey();
        SimpleMailMessage message = CreateMessage(to);
        mailSender.send(message);
        return code;

    }

    @Override
    public String createKey() {
        Random random = new Random();
        int key = 100000+ random.nextInt(900000);

        return String.valueOf(key);
    }

    private String getText(){
        StringBuffer buffer = new StringBuffer();
        buffer.append("안녕하세요 ").append("게시판 사이트입니다.").append(System.lineSeparator()).append("아래 인증번호를 사이트에 입력해주세요").append(System.lineSeparator())
                .append(code).append(System.lineSeparator()).append("감사합니다");
        return buffer.toString();
    }
}
