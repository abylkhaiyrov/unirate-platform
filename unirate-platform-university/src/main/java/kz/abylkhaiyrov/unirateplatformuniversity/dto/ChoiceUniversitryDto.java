package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class ChoiceUniversitryDto {

   private UniversityDto university;

    private CourseDto courseDto;

}
