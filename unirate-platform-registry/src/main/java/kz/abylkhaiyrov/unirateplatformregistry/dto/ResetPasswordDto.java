package kz.abylkhaiyrov.unirateplatformregistry.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ResetPasswordDto {

    @NotNull
    private String newPassword;
    @NotNull
    private String email;

    private String resetCode;

}
