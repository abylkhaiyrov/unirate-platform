package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class CreateUniversityDto {

    private String name;

    private String description;

    private String location;

    private BigDecimal rating;

    private Long baseCost;

    private String website;

    private String accreditation;

    private String contactEmail;

    private String logoUrl;

    private Long ratingCount;

    private Long militaryDepartmentCost;

    private Long dormitoryCost;

    private Boolean militaryDepartment;

    private Boolean dormitory;

}
