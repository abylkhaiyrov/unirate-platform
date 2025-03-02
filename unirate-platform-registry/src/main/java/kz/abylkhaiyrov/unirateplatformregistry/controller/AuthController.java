package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.dto.ResetPasswordDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.LoginDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Tag(name = "Authentication", description = "Endpoints для аутентификации, регистрации и управления активацией пользователя")
@RestController
@RequestMapping("/open-api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "Авторизация пользователя", description = "Проверяет логин и пароль, возвращает JWT токен")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно авторизован", content = @Content)
    @PostMapping("/login")
    public ResponseEntity<LoginDto> login(@RequestBody @Valid LoginDto authLoginDto) {
        return ResponseEntity.ok(authService.login(authLoginDto));
    }

    @Operation(summary = "Регистрация пользователя", description = "Регистрирует нового пользователя")
    @ApiResponse(responseCode = "202", description = "Регистрация пользователя принята", content = @Content)
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody @Valid UserRegisterDto userRegistrationDto) {
        authService.register(userRegistrationDto);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Сброс пароля", description = "Обновляет пароль пользователя")
    @ApiResponse(responseCode = "200", description = "Пароль успешно сброшен", content = @Content)
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody @Valid ResetPasswordDto resetPasswordDto) {
        authService.resetPassword(resetPasswordDto);
        return ResponseEntity.ok("Successfully reset password: " + resetPasswordDto.getNewPassword());
    }

    @Operation(summary = "Активация аккаунта", description = "Активирует аккаунт пользователя по переданному активационному коду")
    @ApiResponse(responseCode = "200", description = "Аккаунт успешно активирован", content = @Content)
    @ApiResponse(responseCode = "400", description = "Неверный или устаревший активационный код", content = @Content)
    @PostMapping("/activation")
    public ResponseEntity<String> activateUser(
            @Parameter(description = "Активационный код", required = true)
            @RequestParam Integer code) {
        return ResponseEntity.ok(authService.activationCode(code));
    }

    @Operation(summary = "Повторная отправка активационного кода", description = "Отправляет новый активационный код на email пользователя")
    @ApiResponse(responseCode = "200", description = "Новый активационный код отправлен", content = @Content)
    @ApiResponse(responseCode = "404", description = "Пользователь с таким email не найден", content = @Content)
    @PostMapping("/resend-activation")
    public ResponseEntity<String> resendActivation(
            @Parameter(description = "Email пользователя", required = true)
            @RequestParam String email) {
        return ResponseEntity.ok(authService.resendActivation(email));
    }
}