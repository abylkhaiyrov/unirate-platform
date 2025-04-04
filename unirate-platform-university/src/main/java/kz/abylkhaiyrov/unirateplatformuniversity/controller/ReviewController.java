package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@Tag(name = "Review API", description = "Операции для управления отзывами")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Создание нового отзыва",
            description = "Создаёт новый отзыв и возвращает его данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно создан", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ReviewReturnDto createReview(@RequestBody CreateReviewDto reviewDto) {
        return reviewService.create(reviewDto);
    }

    @Operation(
            summary = "Обновление отзыва",
            description = "Обновляет данные существующего отзыва",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно обновлён", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @PutMapping("/{reviewId}")
    public ReviewReturnDto updateReview(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId,
            @RequestBody ReviewDto reviewDto) {
        return reviewService.update(reviewId, reviewDto);
    }

    @Operation(
            summary = "Удаление отзыва",
            description = "Удаляет отзыв по заданному ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно удалён", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @DeleteMapping("/{reviewId}")
    public void deleteReview(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
    }

    @Operation(
            summary = "Модерация отзыва",
            description = "Модерирует отзыв, устанавливая статус APPROVED или REJECTED",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв успешно модерирован", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @PutMapping("/{reviewId}/moderate")
    public ReviewReturnDto moderateReview(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId,
            @Parameter(description = "Одобрить отзыв", required = true) @RequestParam boolean approve) {
        return reviewService.moderate(reviewId, approve);
    }

    @Operation(
            summary = "Получение отзыва по ID",
            description = "Возвращает данные отзыва с полной информацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Отзыв найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @GetMapping("/{reviewId}")
    public ReviewReturnDto getReviewById(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId) {
        return reviewService.getById(reviewId);
    }

    @Operation(
            summary = "Получение всех отзывов",
            description = "Возвращает постраничный список всех отзывов",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public Page<ReviewReturnDto> getAllReviews(Pageable pageable) {
        return reviewService.getAll(pageable);
    }

    @Operation(
            summary = "Получение отзывов для конкретного университета",
            description = "Возвращает постраничный список отзывов для указанного университета",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список отзывов успешно получен", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @GetMapping("/university/{forumId}")
    public Page<ReviewReturnDto> getReviewsByUniversity(
            @Parameter(description = "ID форума", required = true) @PathVariable Long forumId,
            Pageable pageable) {
        return reviewService.getByForum(forumId, pageable);
    }

    @Operation(
            summary = "Добавление лайка к отзыву",
            description = "Добавляет лайк к отзыву и возвращает обновлённые данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Лайк успешно добавлен", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @PostMapping("/{reviewId}/like")
    public ReviewReturnDto addLike(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId) {
        return reviewService.addLike(reviewId);
    }

    @Operation(
            summary = "Добавление дизлайка к отзыву",
            description = "Добавляет дизлайк к отзыву и возвращает обновлённые данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Дизлайк успешно добавлен", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @PostMapping("/{reviewId}/dislike")
    public ReviewReturnDto addDislike(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId) {
        return reviewService.addDislike(reviewId);
    }

    @Operation(
            summary = "Получение статистики отзывов для университета",
            description = "Возвращает агрегированную статистику отзывов (средний рейтинг и количество отзывов) для заданного университета",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Статистика успешно получена", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Университет не найден", content = @Content)
            }
    )
    @GetMapping("/university/{universityId}/stats")
    public UniversityReviewStats getUniversityReviewStats(
            @Parameter(description = "ID университета", required = true) @PathVariable Long universityId) {
        return reviewService.getStatsForUniversity(universityId);
    }

    @Operation(
            summary = "Добавление комментария к отзыву",
            description = "Добавляет комментарий к отзыву и возвращает данные созданного комментария",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Комментарий успешно добавлен", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @PostMapping("/{reviewId}/comments")
    public ReviewCommentDto addComment(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId,
            @RequestBody ReviewCommentDto commentDto) {
        return reviewService.addComment(reviewId, commentDto);
    }

    @Operation(
            summary = "Получение комментариев к отзыву",
            description = "Возвращает список комментариев для указанного отзыва",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Список комментариев успешно получен", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Отзыв не найден", content = @Content)
            }
    )
    @GetMapping("/{reviewId}/comments")
    public List<ReviewCommentDto> getComments(
            @Parameter(description = "ID отзыва", required = true) @PathVariable Long reviewId) {
        return reviewService.getComments(reviewId);
    }
}