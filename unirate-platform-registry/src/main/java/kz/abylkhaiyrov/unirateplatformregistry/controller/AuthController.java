package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.dto.ResetPasswordDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.LoginDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.service.AuthService;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints for authentication, registration, and account activation management")
@RestController
@RequestMapping("/open-api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;

    @Operation(
            summary = "User authentication",
            description = "Verifies username and password, returns a JWT token"
    )
    @ApiResponse(responseCode = "200", description = "User successfully authenticated", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto) {
        return ResponseEntity.ok(authService.login(loginDto));
    }

    @Operation(
            summary = "User registration",
            description = "Registers a new user"
    )
    @ApiResponse(responseCode = "202", description = "User registration accepted", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody @Valid UserRegisterDto registerDto) {
        authService.register(registerDto);
        return ResponseEntity.accepted().build();
    }

    @Operation(
            summary = "Request password reset code",
            description = "Sends a reset code to the specified email to confirm a password reset"
    )
    @ApiResponse(responseCode = "200", description = "Password reset code sent successfully", content = @Content)
    @PostMapping("/send-reset-password-code")
    public ResponseEntity<String> sendResetPasswordCode(@RequestParam String email) {
        authService.sendResetPasswordCode(email);
        return ResponseEntity.ok("Password reset code sent successfully");
    }

    @Operation(
            summary = "Reset password",
            description = "Updates the user's password if the provided reset code is valid"
    )
    @ApiResponse(responseCode = "200", description = "Password successfully changed", content = @Content)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDto dto) {
        authService.resetPassword(dto);
        return ResponseEntity.ok("Password successfully changed");
    }

    @Operation(
            summary = "Verify password reset code",
            description = "Verifies if the provided reset code is valid and not expired"
    )
    @ApiResponse(responseCode = "200", description = "Reset code is valid", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid or expired reset code", content = @Content)
    @PostMapping("/verify-reset-password-code")
    public ResponseEntity<String> verifyResetPasswordCode(@RequestParam String email, @RequestParam Integer resetCode) {
        authService.verifyResetPasswordCode(email, resetCode);
        return ResponseEntity.ok("Reset code is valid");
    }

    @Operation(
            summary = "Account activation",
            description = "Activates a user account using the provided activation code"
    )
    @ApiResponse(responseCode = "200", description = "Account successfully activated", content = @Content)
    @ApiResponse(responseCode = "400", description = "Invalid or expired activation code", content = @Content)
    @PostMapping("/activation")
    public ResponseEntity<String> activateUser(
            @Parameter(description = "Activation code", required = true)
            @RequestParam Integer code
    ) {
        return ResponseEntity.ok(authService.activationCode(code));
    }

    @Operation(
            summary = "Resend activation code",
            description = "Sends a new activation code to the user's email"
    )
    @ApiResponse(responseCode = "200", description = "New activation code sent", content = @Content)
    @ApiResponse(responseCode = "404", description = "User with this email not found", content = @Content)
    @PostMapping("/resend-activation")
    public ResponseEntity<String> resendActivation(
            @Parameter(description = "User's email address", required = true)
            @RequestParam String email
    ) {
        return ResponseEntity.ok(authService.resendActivation(email));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns user data by the specified ID"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "Identifier of the user", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findById(id));
    }
}