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
import java.util.Objects;
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

    public String login(LoginDto authLoginDto) {
        var userDetails = personDetailsService.loadUserByUsername(authLoginDto.getEmail());
        if (!passwordEncoder.matches(authLoginDto.getPassword(), userDetails.getPassword())) {
            throw new IllegalArgumentException("Password was incorrect");
        }

        return jwtUtil.generateToken(authLoginDto.getEmail());
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
    public String sendResetPasswordCode(String email) {
        var userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "Пользователь с таким email не найден";
        }
        var user = userOptional.get();

        var resetCode = generateResetCode();
        user.setActivationCode(resetCode);
        user.setActivationCodeSentAt(LocalDateTime.now());
        userService.save(user);

        sendResetPasswordEmail(user.getEmail(), resetCode);
        return "Код для сброса пароля отправлен на email";
    }

    @Transactional
    public String resetPassword(ResetPasswordDto dto) {
        var userOptional = userService.findByEmail(dto.getEmail());
        if (userOptional.isEmpty()) {
            return "Пользователь с таким email не найден";
        }
        var user = userOptional.get();

        if (!Objects.equals(user.getActivationCode(), dto.getResetCode())) {
            return "Неверный код сброса пароля";
        }

        if (user.getActivationCodeSentAt() == null ||
                user.getActivationCodeSentAt().plusHours(24).isBefore(LocalDateTime.now())) {
            return "Код сброса пароля устарел. Пожалуйста, запросите повторную отправку кода.";
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setActivationCode(null);
        user.setActivationCodeSentAt(null);
        userService.save(user);
        return "Пароль успешно изменен";
    }

    public void sendResetPasswordEmail(String email, Integer resetCode) {
        log.info("Отправка email для сброса пароля на адрес {}", email);
        var subject = "Ваш код для сброса пароля";
        var body = "Здравствуйте!\n\n" +
                "Вы запросили сброс пароля. Для подтверждения операции используйте следующий код:\n\n" +
                resetCode +
                "\n\n" +
                "Код действителен в течение 24 часов. Если вы не запрашивали сброс пароля, проигнорируйте это сообщение.\n\n" +
                "С уважением,\nКоманда поддержки";
        mailSender.sendMail(email, subject, body);
    }

    private Integer generateResetCode() {
        return (int)(Math.random() * 900000) + 100000;
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
        var subject = "Ваш код подтверждения для регистрации в UniRate";
        var body = "Приветствуем вас!\n" +
                "\n" +
                "Вы зарегистрировались на платформе UniRate. Для подтверждения вашего email используйте следующий код:\n" +
                "\n"
                + activationCode +
                "\n" +
                "Код действует в течение 24 часов с момента получения письма. Если вы не запрашивали этот код, просто проигнорируйте это письмо.add\n" +
                "\n" +
                "С уважением,\n" +
                "Команда UniRate ";
        mailSender.sendMail(email, subject, body);
    }

    private Integer generateActivationCode() {
        return new Random().nextInt(9000) + 1000;
    }

}
