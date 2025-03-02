package kz.abylkhaiyrov.unirateplatformregistry.service;

import kz.abylkhaiyrov.unirateplatformregistry.adapter.UserAdapter;
import kz.abylkhaiyrov.unirateplatformregistry.dto.ResetPasswordDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.LoginDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.enums.Role;
import kz.abylkhaiyrov.unirateplatformregistry.security.JwtUtil;
import kz.abylkhaiyrov.unirateplatformregistry.security.PersonDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Random;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final PersonDetailsService personDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final UserAdapter userMapper;
    private final UserRoleService userRoleService;
    private final MailSender mailSender;

    public LoginDto login(LoginDto authLoginDto) {
        var userDetails = personDetailsService.loadUserByUsername(authLoginDto.getLoginName());
        if (!passwordEncoder.matches(authLoginDto.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Password was incorrect");
        }

        authLoginDto.setToken(jwtUtil.generateToken(authLoginDto.getLoginName()));
        return authLoginDto;
    }

    @Transactional
    public void register(UserRegisterDto userRegistrationDto) {
        var activeByLogin = userService.findActiveByEmail(userRegistrationDto.getEmail());
        if (activeByLogin.isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }
        var user = new User();
        user.setEmail(userRegistrationDto.getEmail());
        user.setUsername(userRegistrationDto.getUsername());
        user.setActivationCode(generateActivationCode());
        user.setActivationCodeSentAt(LocalDateTime.now());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        sendActivationEmail(user.getEmail(), user.getActivationCode());
        userService.save(user);
        userRoleService.createUserRole(user, Role.USER);
    }

    @Transactional
    public void resetPassword(ResetPasswordDto dto) {
        var activeByLogin = userService.findActiveByEmail(dto.getLogin());
        if (activeByLogin.isEmpty()) {
            throw new IllegalArgumentException("User is not present");
        }
        var user = activeByLogin.get();
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userService.save(user);
    }

    public String activationCode(Integer code) {
        var userOptional = userService.findByActivationCode(code);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            if (user.isActive()) {
                return "Пользователь уже активирован";
            }
            var sentAt = user.getActivationCodeSentAt();
            if (sentAt != null && sentAt.plusHours(24).isBefore(LocalDateTime.now())) {
                return "Активационный код устарел. Пожалуйста, запросите повторную отправку кода.";
            }
            user.setActive(true);
            user.setActivationCode(null);
            user.setActivationCodeSentAt(null);
            userService.save(user);
            return "Пользователь успешно активирован";
        }
        return "Неверный или устаревший активационный код";
    }

    public String resendActivation(String email) {
        var userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "Пользователь с таким email не найден";
        }
        var user = userOptional.get();
        if (user.isActive()) {
            return "Пользователь уже активирован";
        }
        var activationCode = generateActivationCode();
        user.setActivationCode(activationCode);
        user.setActivationCodeSentAt(LocalDateTime.now());
        userService.save(user);
        sendActivationEmail(user.getEmail(), activationCode);

        return "Новый код отправлен на email";
    }

    public void sendActivationEmail(String email, Integer activationCode) {
        log.info("Sending activation email to {}", email);
        var subject = "Activation code on UniRate";
        var body = "Ваш код для активации: " + activationCode;
        mailSender.sendMail(email, subject, body);
    }

    private Integer generateActivationCode() {
        return new Random().nextInt(9000) + 1000;
    }

}
