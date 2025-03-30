package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Favorite;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UniversityAdapter {

    private final UniversityAddressMapper universityAddressMapper;
    private final FacultyMapper facultyMapper;

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

    public UniversityDto entity2Dto(University entity , UniversityAddress universityAddress, List<FacultyAndSpecialityDto> faculty) {
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
        dto.setFaculty(faculty);
        dto.setUniversityAddress(universityAddressDto);
        return dto;
    }

    public UniversityFavDto entity2DtoFromFav(Favorite favorite, University entity , UniversityAddress universityAddress, List<FacultyDto> faculty) {
        var dto = new UniversityFavDto();
        dto.setId(favorite.getId());
        dto.setUniversityId(entity.getId());
        dto.setUserId(favorite.getUserId());
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
        dto.setFaculty(faculty);
        dto.setUniversityAddress(universityAddressDto);
        return dto;
    }

}
