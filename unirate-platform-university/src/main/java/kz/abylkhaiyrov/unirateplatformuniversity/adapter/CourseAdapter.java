package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.CourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseAdapter {

    @Mapping(target = "universityId", source = "university.id")
    CourseDto dto2Entity(Course course);

}
