package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.CourseAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final UniversityService universityService;
    private final CourseRepository courseRepository;
    private final CourseAdapter adapter;

    public CourseDto create(CourseDto dto) {
        log.info("Creating course dto: {}", dto);
        var entity = new Course();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLanguage(dto.getLanguage());
        entity.setRequirements(dto.getRequirements());
        entity.setDurationYears(dto.getDurationYears());
        entity.setStudyMode(dto.getStudyMode());
        entity.setTuitionFee(dto.getTuitionFee());
        entity.setUniversity(universityService.getUniversityById(dto.getUniversityId()));
        saveCourse(entity);
        return adapter.entity2Dto(entity);
    }

    private void saveCourse(Course entity) {
        courseRepository.save(entity);
    }

    public CourseDto get(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id cannot be null");
        }
        var course =  courseRepository.findById(id).orElseThrow(() -> new NotFoundException("Course not found with id: " + id));
        return adapter.entity2Dto(course);
    }

}
