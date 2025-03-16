package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.FacultyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    @Mapping(target = "id" , source = "id")
    @Mapping(target = "universityId", source = "university.id")
    @Mapping(target = "contactPhoneNumber", source = "contactPhone")
    FacultyDto entity2Dto(Faculty faculty);

}
