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
            return "User with this email not found";
        }
        var user = userOptional.get();

        var resetCode = generateActivationCode();
        user.setActivationCode(resetCode);
        user.setActivationCodeSentAt(LocalDateTime.now());
        userService.save(user);

        sendResetPasswordEmail(user.getEmail(), resetCode);
        return "Password reset code has been sent to your email";
    }

    @Transactional
    public String resetPassword(ResetPasswordDto dto) {
        var userOptional = userService.findByEmail(dto.getEmail());
        if (userOptional.isEmpty()) {
            return "User with this email not found";
        }
        var user = userOptional.get();

        if (!Objects.equals(user.getActivationCode(), dto.getResetCode())) {
            return "Invalid password reset code";
        }

        if (user.getActivationCodeSentAt() == null ||
                user.getActivationCodeSentAt().plusHours(24).isBefore(LocalDateTime.now())) {
            return "Password reset code has expired. Please request a new code.";
        }

        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        user.setActivationCode(null);
        user.setActivationCodeSentAt(null);
        userService.save(user);
        return "Password successfully changed";
    }

    public void sendResetPasswordEmail(String email, Integer resetCode) {
        log.info("Sending password reset email to {}", email);
        var subject = "Your Password Reset Code";
        var body = "Hello,\n\n" +
                "You requested a password reset. Please use the following code to confirm this operation:\n\n" +
                resetCode +
                "\n\n" +
                "This code is valid for 24 hours. If you did not request a password reset, please ignore this email.\n\n" +
                "Best regards,\n" +
                "Support Team";
        mailSender.sendMail(email, subject, body);
    }

    public String activationCode(Integer code) {
        var userOptional = userService.findByActivationCode(code);
        if (userOptional.isPresent()) {
            var user = userOptional.get();
            if (user.isActive()) {
                return "User is already activated";
            }
            var sentAt = user.getActivationCodeSentAt();
            if (sentAt != null && sentAt.plusHours(24).isBefore(LocalDateTime.now())) {
                return "Activation code has expired. Please request a new code.";
            }
            user.setActive(true);
            user.setActivationCode(null);
            user.setActivationCodeSentAt(null);
            userService.save(user);
            return "User successfully activated";
        }
        return "Invalid or expired activation code";
    }

    @Transactional
    public String resendActivation(String email) {
        var userOptional = userService.findByEmail(email);
        if (userOptional.isEmpty()) {
            return "User with this email not found";
        }
        var user = userOptional.get();
        if (user.isActive()) {
            return "User is already activated";
        }
        var activationCode = generateActivationCode();
        user.setActivationCode(activationCode);
        user.setActivationCodeSentAt(LocalDateTime.now());
        userService.save(user);

        sendActivationEmail(user.getEmail(), activationCode);
        return "A new activation code has been sent to your email";
    }

    public void sendActivationEmail(String email, Integer activationCode) {
        log.info("Sending activation email to {}", email);
        var subject = "Your UniRate Registration Confirmation Code";
        var body = "Welcome!\n\n" +
                "You have registered on the UniRate platform. Please use the following code to confirm your email address:\n\n" +
                activationCode +
                "\n\n" +
                "This code is valid for 24 hours from receipt. If you did not request this code, please ignore this email.\n\n" +
                "Best regards,\n" +
                "The UniRate Team";
        mailSender.sendMail(email, subject, body);
    }

    private Integer generateActivationCode() {
        return new Random().nextInt(9000) + 1000;
    }

}
