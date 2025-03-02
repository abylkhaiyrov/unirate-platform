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
    private String login;

    public ResetPasswordDto(String newPassword, String login) {
        super(login);
        this.login = login;
        this.newPassword = newPassword;
    }

    public String getLogin() {
        return super.getLogin();
    }
}
