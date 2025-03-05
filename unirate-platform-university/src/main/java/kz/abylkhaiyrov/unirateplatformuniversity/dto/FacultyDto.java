package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class FacultyDto {
    private Long id;
    private String name;
    private String description;
    private String contactEmail;
    @Pattern(regexp = "^(?:\\+7|8)7\\d{9}$", message = "Telephone number should in format: +77764268111 or " +
            "87764268111")
    private String contactPhoneNumber;
    private Long universityId;
}
