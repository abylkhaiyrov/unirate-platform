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
        String subject = "Thank you for subscribing to UniRate";
        String body = "Hello,\n\n" +
                "You have successfully subscribed to notifications about new posts on the UniRate site.\n\n" +
                "Thank you for staying with us!\n\n" +
                "Best regards,\n" +
                "The UniRate Team";
        sendMail(email, subject, body);
    }

}
