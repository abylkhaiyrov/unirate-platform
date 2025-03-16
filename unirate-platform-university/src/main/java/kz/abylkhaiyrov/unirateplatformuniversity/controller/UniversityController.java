package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateUniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversitySearchDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/universities")
@Tag(name = "University API", description = "Операции для управления университетами")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;
    private final UniversityAdapter universityAdapter;

    @Operation(
            summary = "Создание нового университета",
            description = "Создаёт новый университет и возвращает его данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Университет успешно создан", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public UniversityDto createUniversity(@RequestBody CreateUniversityDto universityDto) {
        return universityService.createUniversity(universityDto);
    }

    @Operation(
            summary = "Получение университета по имени",
            description = "Возвращает данные университета по его имени",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Университет найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @GetMapping("/name/{name}")
    public UniversityDto getUniversityByName(
            @Parameter(description = "Имя университета", required = true) @PathVariable String name) {
        return universityService.getUniversityByName(name);
    }

    @Operation(
            summary = "Получение университета по ID",
            description = "Возвращает данные университета по его ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Университет найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public UniversityDto getUniversityById(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id) {
        University university = universityService.getUniversityById(id);
        return universityAdapter.entity2Dto(university);
    }

    @Operation(
            summary = "Добавление рейтинга университету",
            description = "Добавляет новый рейтинг университету и обновляет его среднее значение",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Рейтинг успешно добавлен", content = @Content)
            }
    )
    @PostMapping("/{id}/rating")
    public void addRating(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id,
            @Parameter(description = "Значение рейтинга", required = true) @RequestParam Integer rating) {
        universityService.plusRating(rating, id);
    }

    @Operation(
            summary = "Получение всех университетов",
            description = "Возвращает список всех университетов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список университетов успешно получен", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public List<UniversityDto> getAllUniversities() {
        return universityService.getAllUniversities();
    }

    @Operation(
            summary = "Получение университетов с пагинацией",
            description = "Возвращает страницу университетов согласно переданным параметрам",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Страница университетов успешно получена", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/page")
    public Page<UniversityDto> getUniversitiesByPage(Pageable pageable) {
        return universityService.getUniversitiesByPage(pageable);
    }

    @Operation(
            summary = "Обновление данных университета",
            description = "Обновляет данные существующего университета",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Данные университета успешно обновлены", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public UniversityDto updateUniversity(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id,
            @RequestBody CreateUniversityDto universityDto) {
        return universityService.updateUniversity(id, universityDto);
    }

    @Operation(
            summary = "Удаление (деактивация) университета",
            description = "Деактивирует университет по заданному ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Университет успешно деактивирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void deleteUniversity(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id) {
        universityService.deleteUniversity(id);
    }

    @Operation(
            summary = "Поиск университетов",
            description = "Ищет университеты по ключевому слову в имени или описании",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Результаты поиска успешно получены", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/search")
    public Page<UniversityDto> searchUniversities(
            @RequestBody UniversitySearchDto universitySearchDto) {
        return universityService.searchUniversities(universitySearchDto);
    }

    @Operation(
            summary = "Получение топ университетов",
            description = "Возвращает список топ университетов, отсортированных по рейтингу",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Топ университетов успешно получен", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping("/top")
    public List<UniversityDto> getTopUniversities(
            @Parameter(description = "Лимит количества университетов", required = true) @RequestParam Integer limit) {
        return universityService.getTopUniversities(limit);
    }

    @Operation(
            summary = "Обновление логотипа университета",
            description = "Обновляет URL логотипа университета",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Логотип успешно обновлён", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @PutMapping("/{id}/logo")
    public UniversityDto updateLogo(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id,
            @Parameter(description = "Новый URL логотипа", required = true) @RequestParam String logoUrl) {
        return universityService.updateLogo(id, logoUrl);
    }

    @Operation(
            summary = "Активация или деактивация университета",
            description = "Устанавливает статус активности университета",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статус университета успешно обновлён", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @PutMapping("/{id}/active")
    public UniversityDto activateUniversity(
            @Parameter(description = "ID университета", required = true) @PathVariable Long id,
            @Parameter(description = "Статус активности", required = true) @RequestParam Boolean active) {
        return universityService.activateUniversity(id, active);
    }
}