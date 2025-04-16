package kz.abylkhaiyrov.unirateplatformregistry.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class MailSender {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String to, String subject, String body) {
        log.info("Sending email to {}, subject {}", to, subject);
        var mailMessage = new SimpleMailMessage();

        mailMessage.setFrom(username);
        mailMessage.setTo(to);
        mailMessage.setSubject(subject);
        mailMessage.setText(body);

        javaMailSender.send(mailMessage);
    }

    public void sendSubscriptionConfirmationEmail(String email) {
        log.info("Sending subscription confirmation email to {}", email);
        String subject = "Спасибо за подписку на UniRate";
        String body = "Здравствуйте!\n\n" +
                "Вы успешно подписались на уведомления о новых постах с сайта UniRate.\n\n" +
                "Спасибо, что остаетесь с нами!\n\n" +
                "С уважением,\n" +
                "Команда UniRate";
        sendMail(email, subject, body);
    }

}
