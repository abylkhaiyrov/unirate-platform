package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.service.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Tag(name = "Notifications", description = "Endpoints for subscription to UniRate updates")
@RestController
@RequestMapping("/open-api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final MailSender notificationService;

    @Operation(
            summary = "Subscribe to new post notifications",
            description = "Registers the given email to receive notifications about new posts on UniRate"
    )
    @ApiResponse(responseCode = "200", description = "Subscription confirmed", content = @Content)
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(
            @Parameter(description = "Email address to subscribe", required = true)
            @RequestParam @NotBlank @Email String email
    ) {
        notificationService.sendSubscriptionConfirmationEmail(email);
        return ResponseEntity.ok("Subscription successful. A confirmation email has been sent to " + email);
    }
}