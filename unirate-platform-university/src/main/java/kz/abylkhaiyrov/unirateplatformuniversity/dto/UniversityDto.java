package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class UniversityDto {

    private Long id;

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

    private Boolean militaryDepartment;

    private Boolean dormitory;

    private UniversityAddressDto universityAddress;

    private List<FacultyAndSpecialityDto> faculty;
}
