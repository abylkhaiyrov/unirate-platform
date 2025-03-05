package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.FacultyMapper;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.FacultyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.EmptyException;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final UniversityService universityService;
    private final FacultyMapper facultyMapper;

    private void save(Faculty faculty) {
        facultyRepository.save(faculty);
    }

    public FacultyDto create(FacultyDto dto) {
        log.info("Create faculty: {}", dto);
        var university = universityService.getUniversityById(dto.getUniversityId());
        var entity = new Faculty();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setContactEmail(dto.getContactEmail());
        entity.setContactPhone(dto.getContactPhoneNumber());
        entity.setUniversity(university);
        // Предполагается, что при создании факультет активен по умолчанию
        entity.setActive(true);
        save(entity);
        return facultyMapper.entity2Dto(entity);
    }

    public FacultyDto getById(Long id) {
        log.info("Get faculty by id: {}", id);
        if (id == null) {
            throw new EmptyException("faculty id is null");
        }
        var faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("faculty not found with id:" + id));
        return facultyMapper.entity2Dto(faculty);
    }

    public void deleteById(Long id) {
        var faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Факультет не найден с id: " + id));
        log.info("Деактивация факультета с id: {}", id);
        faculty.setActive(false);
        save(faculty);
    }

    public FacultyDto updateById(Long id, FacultyDto dto) {
        log.info("Update faculty: {}", dto);
        var entity = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("faculty not found with id:" + id));

        updateIfChanged(entity::getName, entity::setName, dto.getName());
        updateIfChanged(entity::getDescription, entity::setDescription, dto.getDescription());
        updateIfChanged(entity::getContactEmail, entity::setContactEmail, dto.getContactEmail());
        updateIfChanged(entity::getContactPhone, entity::setContactPhone, dto.getContactPhoneNumber());

        save(entity);
        return facultyMapper.entity2Dto(entity);
    }

    public FacultyDto getFacultyByName(String name) {
        log.info("Get faculty by name: {}", name);
        return facultyRepository.findByNameContaining(name)
                .map(facultyMapper::entity2Dto)
                .orElse(null);
    }

    /**
     * Получение списка всех активных факультетов.
     */
    public List<FacultyDto> getAllFaculties() {
        log.info("Получение списка всех факультетов");
        return facultyRepository.findAll().stream()
                .filter(Faculty::getActive)
                .map(facultyMapper::entity2Dto)
                .collect(Collectors.toList());
    }

    /**
     * Получение списка факультетов по идентификатору университета.
     *
     * @param universityId идентификатор университета.
     */
    public List<FacultyDto> getFacultiesByUniversityId(Long universityId) {
        log.info("Получение списка факультетов для университета с id: {}", universityId);
        return facultyRepository.findByUniversityId(universityId).stream()
                .filter(Faculty::getActive)
                .map(facultyMapper::entity2Dto)
                .collect(Collectors.toList());
    }

    /**
     * Активация факультета (восстановление, если был деактивирован).
     *
     * @param id идентификатор факультета.
     * @return обновлённый DTO факультета.
     */
    public FacultyDto activateFacultyById(Long id) {
        log.info("Активация факультета с id: {}", id);
        var faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Факультет не найден с id: " + id));
        faculty.setActive(true);
        save(faculty);
        return facultyMapper.entity2Dto(faculty);
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