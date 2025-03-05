package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.CourseAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.Valid;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
@Slf4j
@RequiredArgsConstructor
public class CourseService {

    private final UniversityService universityService;
    private final CourseRepository courseRepository;
    private final CourseAdapter adapter;

    /**
     * Создает новый курс.
     *
     * @param dto объект передачи данных курса, содержащий информацию для создания курса
     * @return созданный курс в виде DTO
     */
    @Transactional
    public CourseDto create(@Valid CourseDto dto) {
        log.info("Создание курса: {}", dto);
        Course entity = new Course();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setLanguage(dto.getLanguage());
        entity.setRequirements(dto.getRequirements());
        entity.setDurationYears(dto.getDurationYears());
        entity.setStudyMode(dto.getStudyMode());
        entity.setTuitionFee(dto.getTuitionFee());
        entity.setUniversity(universityService.getUniversityById(dto.getUniversityId()));

        saveCourse(entity);
        return adapter.dto2Entity(entity);
    }

    /**
     * Возвращает курс по идентификатору.
     *
     * @param id идентификатор курса
     * @return DTO курса
     * @throws IllegalArgumentException если id равен null
     * @throws NotFoundException если курс с указанным id не найден
     */
    @Transactional(readOnly = true)
    public CourseDto getById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("id не может быть null");
        }
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Курс не найден с id: " + id));
        return adapter.dto2Entity(course);
    }

    /**
     * Возвращает страницу активных курсов.
     *
     * @param pageable информация о пагинации
     * @return страница DTO курсов
     */
    @Transactional(readOnly = true)
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseRepository.findAllByActiveTrue(pageable)
                .map(adapter::dto2Entity);
    }

    /**
     * Обновляет курс по идентификатору.
     *
     * @param id идентификатор курса
     * @param dto объект передачи данных с обновляемой информацией
     * @return обновленный DTO курса
     * @throws NotFoundException если курс с указанным id не найден
     */
    @Transactional
    public CourseDto updateCourseById(Long id, @Valid CourseDto dto) {
        Course entity = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Курс не найден с id: " + id));
        log.info("Обновление курса с id {}: {}", id, dto);

        updateIfChanged(entity::getName, entity::setName, dto.getName());
        updateIfChanged(entity::getDescription, entity::setDescription, dto.getDescription());
        updateIfChanged(entity::getLanguage, entity::setLanguage, dto.getLanguage());
        updateIfChanged(entity::getRequirements, entity::setRequirements, dto.getRequirements());
        updateIfChanged(entity::getDurationYears, entity::setDurationYears, dto.getDurationYears());
        updateIfChanged(entity::getStudyMode, entity::setStudyMode, dto.getStudyMode());
        updateIfChanged(entity::getTuitionFee, entity::setTuitionFee, dto.getTuitionFee());

        saveCourse(entity);
        return adapter.dto2Entity(entity);
    }

    /**
     * Отключает (деактивирует) курс по идентификатору.
     *
     * @param id идентификатор курса
     * @throws NotFoundException если курс с указанным id не найден
     */
    @Transactional
    public void deleteById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Курс не найден с id: " + id));
        log.info("Деактивация курса с id: {}", id);
        course.setActive(false);
        saveCourse(course);
    }

    /**
     * Сохраняет курс в репозитории.
     *
     * @param entity объект курса
     */
    private void saveCourse(Course entity) {
        courseRepository.save(entity);
    }

    /**
     * Вспомогательный метод для обновления поля объекта, если новое значение не null и отличается от текущего.
     *
     * @param currentValueSupplier поставщик текущего значения
     * @param setter функция для установки нового значения
     * @param newValue новое значение для поля
     * @param <T> тип поля
     */
    private <T> void updateIfChanged(Supplier<T> currentValueSupplier, Consumer<T> setter, T newValue) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            setter.accept(newValue);
        }
    }
}