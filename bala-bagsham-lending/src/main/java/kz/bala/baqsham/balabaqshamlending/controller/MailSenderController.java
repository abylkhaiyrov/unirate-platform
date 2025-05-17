package kz.bala.baqsham.balabaqshamlending.controller;

import kz.bala.baqsham.balabaqshamlending.dto.AppealDto;
import kz.bala.baqsham.balabaqshamlending.service.AppealMailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.Valid;

@RestController
@RequestMapping("/open-api/mail")
@RequiredArgsConstructor
public class MailSenderController {

    private final AppealMailService mailSender;

    @PostMapping
    public ResponseEntity<String> createAppeal(@Valid @RequestBody AppealDto dto) {
        mailSender.sendAppeal(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("Обращение принято, письмо отправлено");
    }
}