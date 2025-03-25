package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Specialty;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface SpecialtyMapper {

    Specialty dtoToEntity(SpecialtyDto specialtyDto);

    @Mapping(target = "facultyId", source = "faculty.id")
    @Mapping(target = "courses", source = "courses")
    SpecialtyDto entityToDto(Specialty specialty);
}
