package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversityAddressDto {

    private Long id;

    private String city;

    private String region;

    private String fullAddress;

    private Long universityId;

}
