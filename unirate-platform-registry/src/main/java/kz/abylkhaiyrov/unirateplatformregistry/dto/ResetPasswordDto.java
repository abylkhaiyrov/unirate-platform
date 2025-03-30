package kz.abylkhaiyrov.unirateplatformregistry.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ResetPasswordDto extends LoginInterceptor {

    @NotNull
    private String newPassword;
    @NotNull
    private String email;

    public ResetPasswordDto(String newPassword, String email) {
        super(email);
        this.email = email;
        this.newPassword = newPassword;
    }

    public String getEmail() {
        return super.getEmail();
    }
}
