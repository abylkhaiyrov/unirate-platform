package kz.abylkhaiyrov.unirateplatformregistry.dto.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
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
