package messageboard.service.Impl;

import lombok.RequiredArgsConstructor;
import messageboard.Exception.EmailException;
import messageboard.entity.member.EmailVerification;
import messageboard.repository.EmailRepository;
import messageboard.service.EmailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class JoinEmailServiceImpl implements EmailService {

    private final EmailRepository emailRepository;

    @Override
    public String generateVerification(String username) {
        if (!emailRepository.existsByUsername(username)){
            EmailVerification emailVerification = new EmailVerification(username);
            emailVerification=emailRepository.save(emailVerification);
            return emailVerification.getVerificationId();
        }
        return getVerificationIdByUsername(username);
    }

    @Override
    public String getVerificationIdByUsername(String username) {
        EmailVerification verification = emailRepository.findByUsername(username);
        if (verification!=null){
            return verification.getVerificationId();
        }
        return null;
    }

    @Override
    public String getUsernameForVerificationId(String username) {
        Optional<EmailVerification> verification = Optional.ofNullable(emailRepository.findById(username).orElseThrow(() -> new EmailException()));
        if (verification.isPresent()){
            return verification.get().getUsername();
        }
        return null;
    }
}
