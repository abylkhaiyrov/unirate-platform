package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateCourseDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-api/courses")
@Tag(name = "Course API", description = "Операции для управления курсами")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;

    @Operation(
            summary = "Создание нового курса",
            description = "Создает новый курс и возвращает его данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Курс успешно создан", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public CourseDto createCourse(@RequestBody CreateCourseDto courseDto) {
        return courseService.create(courseDto);
    }

    @Operation(
            summary = "Получение курса по ID",
            description = "Возвращает данные курса по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Курс найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Курс не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public CourseDto getCourseById(
            @Parameter(description = "Идентификатор курса", required = true) @PathVariable Long id) {
        return courseService.getById(id);
    }

    @Operation(
            summary = "Получение списка активных курсов",
            description = "Возвращает страницу активных курсов согласно переданным параметрам пагинации",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Страница курсов успешно получена", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/page")
    public Page<CourseDto> getAllCourses(Pageable pageable) {
        return courseService.getAllCourses(pageable);
    }

    @Operation(
            summary = "Обновление курса по ID",
            description = "Обновляет данные курса и возвращает обновленные данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Курс успешно обновлен", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Курс не найден", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public CourseDto updateCourse(
            @Parameter(description = "Идентификатор курса", required = true) @PathVariable Long id,
            @RequestBody CourseDto courseDto) {
        return courseService.updateCourseById(id, courseDto);
    }

    @Operation(
            summary = "Деактивация курса",
            description = "Деактивирует курс по его идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Курс успешно деактивирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Курс не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void deleteCourse(
            @Parameter(description = "Идентификатор курса", required = true) @PathVariable Long id) {
        courseService.deleteById(id);
    }
}