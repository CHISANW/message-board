package messageboard.service;

import org.springframework.mail.SimpleMailMessage;

public interface FindEmailService {

    SimpleMailMessage CreateMessage(String to);

    String sendSimpleMessage(String to);

    String createKey();
}
