package kz.bala.baqsham.balabaqshamlending.config;

import kz.bala.baqsham.balabaqshamlending.dto.ApiError;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailSendException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.servlet.http.HttpServletRequest;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MailSendException.class)
    public ResponseEntity<ApiError> handleMailError(
            MailSendException ex,
            HttpServletRequest request) {
        log.error("Ошибка при отправке письма", ex);
        ApiError err = new ApiError(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Internal Server Error",
                "Не удалось отправить письмо",
                request.getRequestURI());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(err);
    }
}