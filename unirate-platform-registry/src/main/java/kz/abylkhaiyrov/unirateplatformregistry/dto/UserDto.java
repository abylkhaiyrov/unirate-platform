package kz.abylkhaiyrov.unirateplatformregistry.dto;

import kz.abylkhaiyrov.unirateplatformregistry.enums.Role;
import kz.abylkhaiyrov.unirateplatformregistry.enums.UserStatus;
import lombok.*;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;


@Getter
@Setter
@ToString
@Data
public class UserDto {

    private Long id;
    private String username;
    private String password;
    @Email
    private String email;
    private String firstName;
    private String lastName;
    private Role role;
    @Pattern(regexp = "^(?:\\+7|8)7\\d{9}$", message = "Telephone number should in format: +77764268111 or " +
            "87764268111")
    private String telephone;
    private UserStatus status;
}
