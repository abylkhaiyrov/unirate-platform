package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.FacultyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface FacultyMapper {

    FacultyDto entity2Dto(Faculty faculty);

}
