package kz.abylkhaiyrov.unirateplatformregistry.dto.auth;

import lombok.Data;

@Data
public class LoginDto {
    private String email;
    private String password;
}
