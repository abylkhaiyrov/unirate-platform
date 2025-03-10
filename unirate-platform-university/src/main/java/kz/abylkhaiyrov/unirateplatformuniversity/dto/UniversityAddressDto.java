package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
public class UniversityAddressDto {

    @Column(name = "city")
    private String city;

    @Column(name = "region")
    private String region;

    @Column(name = "full_address")
    private String fullAddress;

}
