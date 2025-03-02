package kz.abylkhaiyrov.unirateplatformregistry.dto.auth;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class UserRegisterDto {
    private String username;
    private String password;
    private String email;

    public UserRegisterDto(String username, String password, String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }
}
