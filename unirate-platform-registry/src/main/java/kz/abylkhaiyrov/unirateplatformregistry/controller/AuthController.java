package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.AuthResponseDTO;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.LoginDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.repository.UserRepository;
import kz.abylkhaiyrov.unirateplatformregistry.security.JWTGenerator;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserService;
import kz.abylkhaiyrov.unirateplatformregistry.util.Constans;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Authentication Controller", description = "API для аутентификации пользователями")
@RestController
@RequestMapping(path = Constans.API + Constans.VERSION_1 + Constans.AUTH, produces = "application/json")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JWTGenerator jwtGenerator;

    @Operation(summary = "Создать пользователя", description = "Создает нового пользователя на основе переданных данных")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Пользователь успешно создан",
                    content = {@Content(mediaType = "application/json", schema = @Schema(implementation = User.class))}),
            @ApiResponse(responseCode = "400", description = "Ошибка в переданных данных", content = @Content)
    })
    @PostMapping(path = "/register")
    public ResponseEntity<?> register(@RequestBody UserRegisterDto user) {
        if (userRepository.existsByUsername(user.getUsername()) && userRepository.existsByEmail(user.getEmail())) {
            return new ResponseEntity<>("Username is taken!" , HttpStatus.BAD_REQUEST);
        }
        userService.createUser(user);

        return new ResponseEntity<>("User registered successfully" , HttpStatus.CREATED);
    }

    @PostMapping("login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginDto loginDto){
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDto.getLoginName(),
                        loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtGenerator.generateToken(authentication);
        return new ResponseEntity<>(new AuthResponseDTO(token), HttpStatus.OK);
    }

}
