package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.SpecialtyMapper;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateSpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Specialty;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.CourseRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.FacultyRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SpecialtyService {

    private final SpecialtyRepository specialtyRepository;
    private final SpecialtyMapper specialtyMapper;
    private final FacultyRepository facultyRepository;
    private final CourseRepository courseRepository;

    /**
     * Creates a new Specialty.
     *
     * @param specialtyDto the DTO containing the specialty data
     * @return the created Specialty as a DTO
     */
    public SpecialtyDto createSpecialty(CreateSpecialtyDto specialtyDto) {
        log.info("Creating Specialty: {}", specialtyDto);
        var entity = new Specialty();
        entity.setName(specialtyDto.getName());
        entity.setDescription(specialtyDto.getDescription());

        var faculty = facultyRepository.findById(specialtyDto.getFacultyId())
                .orElseThrow(() -> new NotFoundException("Faculty not found with id: " + specialtyDto.getFacultyId()));
        entity.setFaculty(faculty);
        entity.setFacultyName(faculty.getName());
        entity.setGopCode(specialtyDto.getGopCode());
        entity.setMinScores(specialtyDto.getMinScores());
        entity.setGrants(specialtyDto.getGrants());
        entity.setUniversityName(faculty.getUniversity().getName());
        entity = specialtyRepository.save(entity);

        if (specialtyDto.getCourseIds() != null && !specialtyDto.getCourseIds().isEmpty()) {
            var courses = courseRepository.findAllById(specialtyDto.getCourseIds());

            if (courses.size() != specialtyDto.getCourseIds().size()) {
                throw new NotFoundException("One or more courses not found for ids: " + specialtyDto.getCourseIds());
            }

            final var savedEntity = entity;
            courses.forEach(course -> {
                course.getSpecialties().add(savedEntity);
                courseRepository.save(course);
            });

            entity.setCourses(courses);
            entity = specialtyRepository.save(entity);
        }

        return specialtyMapper.entityToDto(entity);
    }

    /**
     * Retrieves a Specialty by its id.
     *
     * @param id the id of the Specialty
     * @return the Specialty as a DTO
     * @throws NotFoundException if the Specialty is not found
     */
    public SpecialtyDto getSpecialtyById(Long id) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty not found with id: " + id));
        return specialtyMapper.entityToDto(specialty);
    }

    /**
     * Retrieves all Specialties.
     *
     * @return a list of Specialty DTOs
     */
    public List<SpecialtyDto> getAllSpecialties() {
        return specialtyRepository.findAll()
                .stream()
                .map(specialtyMapper::entityToDto)
                .collect(Collectors.toList());
    }

    /**
     * Updates an existing Specialty.
     *
     * @param id           the id of the Specialty to update
     * @param specialtyDto the DTO with updated data
     * @return the updated Specialty as a DTO
     * @throws NotFoundException if the Specialty is not found
     */
    public SpecialtyDto updateSpecialty(Long id, SpecialtyDto specialtyDto) {
        Specialty specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty not found with id: " + id));

        updateIfChanged(specialty::getName, specialty::setName, specialtyDto.getName());
        updateIfChanged(specialty::getDescription, specialty::setDescription, specialtyDto.getDescription());
        updateIfChanged(specialty::getGopCode, specialty::setGopCode, specialtyDto.getGopCode());
        updateIfChanged(specialty::getMinScores, specialty::setMinScores, specialtyDto.getMinScores());
        updateIfChanged(specialty::getGrants, specialty::setGrants, specialtyDto.getGrants());
        specialty = specialtyRepository.save(specialty);
        return specialtyMapper.entityToDto(specialty);
    }


    /**
     * Deletes a Specialty by its id.
     *
     * @param id the id of the Specialty to delete
     * @throws NotFoundException if the Specialty is not found
     */
    public void deleteSpecialty(Long id) {
        var specialty = specialtyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Specialty not found with id: " + id));
        specialty.setActive(false);
        specialtyRepository.save(specialty);
        log.info("Deleted Specialty with id: {}", id);
    }


    public String updateSpecialtyProfile(Long specialtyId, String url) {
        var user = specialtyRepository.findById(specialtyId)
                .orElseThrow(() -> new IllegalArgumentException("Specialty doesn't exist with id: " + specialtyId));
        try {
            if (url != null && (user.getSpecialtyImageUrl() == null || !user.getSpecialtyImageUrl().equals(url))) {
                user.setSpecialtyImageUrl(url);
            }
            specialtyRepository.save(user);
            return "Specialty updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Specialty update failed";
        }
    }

    /**
     * Вспомогательный метод для обновления поля объекта, если новое значение не null и отличается от текущего.
     *
     * @param currentValueSupplier поставщик текущего значения.
     * @param setter функция для установки нового значения.
     * @param newValue новое значение для поля.
     * @param <T> тип поля.
     */
    private <T> void updateIfChanged(Supplier<T> currentValueSupplier, Consumer<T> setter, T newValue) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            setter.accept(newValue);
        }
    }

}
