package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UniversityAdapter {

    private final UniversityAddressMapper universityAddressMapper;

    public UniversityDto entity2Dto(University entity) {
        var dto = new UniversityDto();
        dto.setId(entity.getId());
        dto.setAccreditation(entity.getAccreditation());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setContactEmail(entity.getContactEmail());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setBaseCost(entity.getBaseCost());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setRatingCount(entity.getRatingCount());
        dto.setDormitory(entity.getDormitory());
        dto.setMilitaryDepartment(entity.getMilitaryDepartment());
        return dto;
    }

    public UniversityDto entity2Dto(University entity , UniversityAddress universityAddress) {
        var dto = new UniversityDto();
        dto.setId(entity.getId());
        dto.setAccreditation(entity.getAccreditation());
        dto.setName(entity.getName());
        dto.setDescription(entity.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setContactEmail(entity.getContactEmail());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setBaseCost(entity.getBaseCost());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setRatingCount(entity.getRatingCount());
        dto.setDormitory(entity.getDormitory());
        dto.setMilitaryDepartment(entity.getMilitaryDepartment());
        var universityAddressDto = universityAddressMapper.entity2Dto(universityAddress);
        dto.setUniversityAddress(universityAddressDto);
        return dto;
    }

}
