package kz.abylkhaiyrov.unirateplatformuniversity.adapter;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.CourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseAdapter {

    public CourseDto entity2Dto(Course course) {
        var dto = new CourseDto();
        dto.setId(course.getId());
        dto.setName(course.getName());
        dto.setDescription(course.getDescription());
        dto.setLanguage(course.getLanguage());
        dto.setRequirements(course.getRequirements());
        dto.setDurationYears(course.getDurationYears());
        dto.setStudyMode(course.getStudyMode());
        dto.setTuitionFee(course.getTuitionFee());
        dto.setUniversityId(dto.getUniversityId());
        return dto;
    }

}
