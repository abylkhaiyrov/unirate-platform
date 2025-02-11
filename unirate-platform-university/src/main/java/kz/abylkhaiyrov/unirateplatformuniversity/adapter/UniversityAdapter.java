package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import org.springframework.stereotype.Component;

@Component
public class UniversityAdapter {

    public UniversityDto entity2Dto(University entity) {
        var dto = new UniversityDto();
        dto.setAccreditation(entity.getAccreditation());
        dto.setName(entity.getName());
        dto.setDescription(dto.getDescription());
        dto.setLocation(entity.getLocation());
        dto.setContactEmail(entity.getContactEmail());
        dto.setLogoUrl(entity.getLogoUrl());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setBaseCost(entity.getBaseCost());
        dto.setWebsite(entity.getWebsite());
        dto.setRating(entity.getRating());
        dto.setRatingCount(entity.getRatingCount());
        return dto;
    }

}
