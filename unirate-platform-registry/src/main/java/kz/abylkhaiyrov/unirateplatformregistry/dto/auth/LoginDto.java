package kz.abylkhaiyrov.unirateplatformregistry.dto.auth;

import lombok.Data;

@Data
public class LoginDto {
    private String loginName;
    private String password;
}
