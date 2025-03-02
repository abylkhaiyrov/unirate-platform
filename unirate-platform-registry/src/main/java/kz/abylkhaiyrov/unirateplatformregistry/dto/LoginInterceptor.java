package kz.abylkhaiyrov.unirateplatformregistry.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public abstract class LoginInterceptor {

    private String login;

    public LoginInterceptor(String login) {
        this.login = login;
    }

    public String getLogin() {
        if (this.login == null) {
            return "";
        }
        int i = this.login.indexOf("@");
        return this.login.substring(0, i);
    }

}
