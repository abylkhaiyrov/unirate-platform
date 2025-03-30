package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateFavoriteDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonRequestDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityFavDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/favorites")
@Tag(name = "Favorite API", description = "Операции для управления избранными университетами")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(
            summary = "Создание нового избранного",
            description = "Создает новую запись избранного и возвращает данные университета с дополнительной информацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Избранное успешно создано", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public UniversityFavDto createFavorite(@RequestBody CreateFavoriteDto createFavoriteDto) {
        return favoriteService.createFavorite(createFavoriteDto);
    }

    @Operation(
            summary = "Получение избранных по userId",
            description = "Возвращает список избранных университетов для заданного пользователя",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Избранное найдено", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Избранное не найдено", content = @Content)
            }
    )
    @GetMapping("/user/{userId}")
    public List<UniversityFavDto> getAllFavoritesByUserId(
            @Parameter(description = "ID пользователя", required = true) @PathVariable Long userId) {
        return favoriteService.getAllByUserId(userId);
    }

    @Operation(
            summary = "Удаление избранного",
            description = "Удаляет запись избранного по ее ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Избранное успешно удалено", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Избранное не найдено", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void deleteFavorite(
            @Parameter(description = "ID избранного", required = true) @PathVariable Long id) {
        favoriteService.deleteById(id);
    }

    @Operation(
            summary = "Поиск сравнения университетов",
            description = "Возвращает список сравнения университетов по заданным критериям",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Сравнение успешно выполнено", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/comparison/filter")
    public List<UniversityComparisonDto> searchUniversityComparison(
            @RequestBody UniversityComparisonRequestDto requestDto) {
        return favoriteService.searchUniversityComparison(requestDto);
    }

}