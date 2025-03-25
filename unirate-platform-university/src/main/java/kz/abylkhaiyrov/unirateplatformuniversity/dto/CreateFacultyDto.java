package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Pattern;

@Getter
@Setter
public class CreateFacultyDto {
    private String name;
    private String description;
    private String contactEmail;
    @Pattern(regexp = "^(?:\\+7|8)7\\d{9}$", message = "Telephone number should in format: +77764268111 or " +
            "87764268111")
    private String contactPhoneNumber;
    private Long baseCost;
    private Long universityId;
}
