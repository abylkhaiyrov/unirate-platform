package kz.abylkhaiyrov.unirateplatformregistry.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@Setter
@ToString
public class UserUpdateDto {
    private String firstName;
    private String lastName;
}
