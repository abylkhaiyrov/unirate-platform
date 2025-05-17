package kz.bala.baqsham.balabaqshamlending.service;

import kz.bala.baqsham.balabaqshamlending.dto.AppealDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class AppealMailService {

    private final JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    @Value("${spring.mail.to}")
    private String mailTo;

    /**
     * Отправляет email.
     *
     * @param subject тема письма
     * @param body    содержимое письма (plain-text или HTML)
     * @param html    true — тело интерпретируется как HTML, false — как обычный текст
     */
    public void sendMail(String subject, String body, boolean html) {
        log.info("Sending email, subject: {}", subject);
        try {
            MimeMessage mime = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mime, MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            helper.setFrom(username);
            helper.setTo(mailTo);
            helper.setSubject(subject);
            helper.setText(body, html);
            javaMailSender.send(mime);
        } catch (MessagingException e) {
            throw new MailSendException("Не удалось сформировать письмо", e);
        } catch (MailException e) {
            log.error("Ошибка отправки почты", e);
            throw e;
        }
    }

    public void sendAppeal(AppealDto dto) {
        log.info("Sending appeal: {}", dto);
        var subject = "Новое обращение от %s".formatted(dto.getFio());
        var body = """
        <html>
        <head>
            <meta charset="UTF-8">
            <style>
                body { font-family: Arial, sans-serif; background:#f7f9fc; margin:0 }
                .card { max-width:600px; margin:24px auto; background:#fff; border-radius:12px;
                        box-shadow:0 4px 12px rgba(0,0,0,.08); overflow:hidden }
                .header { background:#4c8bf5; color:#fff; padding:16px 24px; font-size:18px;
                          font-weight:600 }
                .content { padding:24px }
                .field { margin:0 0 12px 0; line-height:1.4 }
                .label  { font-weight:600; color:#4c8bf5 }
                .footer { font-size:12px; color:#6b7280; padding:16px 24px 24px }
            </style>
        </head>
        <body>
            <div class="card">
                <div class="header">Новое обращение</div>
                <div class="content">
                    <p>Добрый день!</p>
                    <p>Поступило новое обращение. Данные заявителя:</p>

                    <div class="field"><span class="label">ФИО:</span> %s</div>
                    <div class="field"><span class="label">Телефон:</span> %s</div>
                    <div class="field"><span class="label">Комментарий:</span> %s</div>

                    <p>Свяжитесь с заявителем при первой возможности.</p>
                </div>
                <div class="footer">
                    Это письмо сгенерировано автоматически, отвечать на него не нужно.
                </div>
            </div>
        </body>
        </html>
        """.formatted(dto.getFio(),
                dto.getPhoneNumber(),
                dto.getComment());

        sendMail(subject, body, true);
    }
}