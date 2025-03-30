package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class UniversityComparisonDto {

    private String universityName;

    private BigDecimal rating;

    private Long baseCost;

    private Boolean militaryDepartment;

    private Boolean dormitory;

    private String facultyName;

}