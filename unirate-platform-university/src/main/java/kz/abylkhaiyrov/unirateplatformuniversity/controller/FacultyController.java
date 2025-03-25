package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateFacultyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.FacultyAndSpecialityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.FacultyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/faculties")
@Tag(name = "Faculty API", description = "Операции для управления факультетами")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @Operation(
            summary = "Создание нового факультета",
            description = "Создаёт новый факультет и возвращает его данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет успешно создан", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public FacultyDto createFaculty(@RequestBody CreateFacultyDto facultyDto) {
        return facultyService.create(facultyDto);
    }

    @Operation(
            summary = "Получение факультета по ID",
            description = "Возвращает данные факультета по его ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Факультет не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public FacultyDto getFacultyById(
            @Parameter(description = "ID факультета", required = true) @PathVariable Long id) {
        return facultyService.getById(id);
    }

    @Operation(
            summary = "Обновление данных факультета",
            description = "Обновляет данные факультета и возвращает обновлённую информацию",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет успешно обновлён", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Факультет не найден", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public FacultyDto updateFaculty(
            @Parameter(description = "ID факультета", required = true) @PathVariable Long id,
            @RequestBody FacultyDto facultyDto) {
        return facultyService.updateById(id, facultyDto);
    }

    @Operation(
            summary = "Удаление (деактивация) факультета",
            description = "Деактивирует факультет по заданному ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет успешно деактивирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Факультет не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void deleteFaculty(
            @Parameter(description = "ID факультета", required = true) @PathVariable Long id) {
        facultyService.deleteById(id);
    }

    @Operation(
            summary = "Получение факультета по имени",
            description = "Возвращает данные факультета, найденного по имени",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет найден", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/name/{name}")
    public FacultyDto getFacultyByName(
            @Parameter(description = "Имя факультета", required = true) @PathVariable String name) {
        return facultyService.getFacultyByName(name);
    }

    @Operation(
            summary = "Получение всех активных факультетов",
            description = "Возвращает список всех активных факультетов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список факультетов успешно получен", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public List<FacultyDto> getAllFaculties() {
        return facultyService.getAllFaculties();
    }

    @Operation(
            summary = "Получение факультетов по ID университета",
            description = "Возвращает список факультетов, принадлежащих заданному университету",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список факультетов успешно получен", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/university/{universityId}")
    public List<FacultyAndSpecialityDto> getFacultiesByUniversityId(
            @Parameter(description = "ID университета", required = true) @PathVariable Long universityId) {
        return facultyService.getFacultiesByUniversityId(universityId);
    }

    @Operation(
            summary = "Активация факультета",
            description = "Активирует факультет по заданному ID, если он был деактивирован",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Факультет успешно активирован", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Факультет не найден", content = @Content)
            }
    )
    @PutMapping("/{id}/activate")
    public FacultyDto activateFaculty(
            @Parameter(description = "ID факультета", required = true) @PathVariable Long id) {
        return facultyService.activateFacultyById(id);
    }
}