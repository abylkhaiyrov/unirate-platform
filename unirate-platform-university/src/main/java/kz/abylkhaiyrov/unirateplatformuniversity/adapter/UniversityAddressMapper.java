package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UniversityAddressMapper {

    @Mapping(target = "id" , source = "id")
    @Mapping(target = "universityId", source = "university.id")
    UniversityAddressDto entity2Dto(UniversityAddress universityAddress);

}
