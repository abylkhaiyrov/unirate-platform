package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.client.UserClient;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.ReviewComment;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.enums.ReviewStatus;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ReviewRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final UserClient userClient;
    private final UniversityService universityService;

    /**
     * Создаёт новый отзыв.
     * Поля createdAt, updatedAt, статус, лайки и вложения устанавливаются по умолчанию.
     */
    public ReviewReturnDto create(ReviewDto dto) {
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        review.setUniversity(universityService.getUniversityById(dto.getUniversityId()));
        review.setUserId(dto.getUserId());
        saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Обновляет отзыв: изменяются комментарий и рейтинг.
     */
    public ReviewReturnDto update(Long reviewId, ReviewDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Удаляет отзыв по идентификатору.
     */
    public void delete(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new NotFoundException("Review not found with id: " + reviewId);
        }
        reviewRepository.deleteById(reviewId);
    }

    /**
     * Модерирует отзыв, устанавливая статус APPROVED или REJECTED.
     */
    public ReviewReturnDto moderate(Long reviewId, boolean approve) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setStatus(approve ? ReviewStatus.APPROVED : ReviewStatus.REJECTED);
        saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Возвращает отзыв по идентификатору с полной информацией.
     */
    public ReviewReturnDto getById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + id));
        return mapToReviewReturnDto(review);
    }

    /**
     * Возвращает постраничный список всех отзывов.
     */
    public Page<ReviewReturnDto> getAll(Pageable pageable) {
        return reviewRepository.findAll(pageable)
                .map(this::mapToReviewReturnDto);
    }

    /**
     * Возвращает отзывы для конкретного университета с возможностью сортировки и фильтрации.
     */
    public Page<ReviewReturnDto> getByUniversity(Long universityId, Pageable pageable) {
        University university = universityService.getUniversityById(universityId);
        return reviewRepository.findByUniversity(university, pageable)
                .map(this::mapToReviewReturnDto);
    }

    /**
     * Добавляет лайк к отзыву.
     */
    public ReviewReturnDto addLike(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setLikes(review.getLikes() + 1);
        saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Добавляет дизлайк к отзыву.
     */
    public ReviewReturnDto addDislike(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setDislikes(review.getDislikes() + 1);
        saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Возвращает агрегированную статистику для университета: средний рейтинг и количество отзывов.
     */
    public UniversityReviewStats getStatsForUniversity(Long universityId) {
        University university = universityService.getUniversityById(universityId);
        List<Review> reviews = reviewRepository.findByUniversity(university);
        double avgRating = reviews.stream()
                .mapToInt(r -> r.getRating() != null ? r.getRating() : 0)
                .average()
                .orElse(0);
        long count = reviews.size();
        return new UniversityReviewStats(avgRating, count);
    }

    /**
     * Добавляет комментарий к отзыву.
     */
    public ReviewCommentDto addComment(Long reviewId, ReviewCommentDto commentDto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));

        ReviewComment comment = new ReviewComment();
        comment.setReview(review);
        comment.setUserId(commentDto.getUserId());
        comment.setComment(commentDto.getComment());
        saveReviewComment(comment);
        commentDto.setId(comment.getId());
        return commentDto;
    }

    /**
     * Возвращает список комментариев к отзыву.
     */
    public List<ReviewCommentDto> getComments(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        return reviewCommentRepository.findByReview(review).stream()
                .map(comment -> {
                    ReviewCommentDto dto = new ReviewCommentDto();
                    dto.setId(comment.getId());
                    dto.setUserId(comment.getUserId());
                    // Получаем имя пользователя через userClient (при наличии)
                    ResponseEntity<UserDto> response = userClient.findUserById(comment.getUserId());
                    UserDto user = (response != null) ? response.getBody() : null;
                    dto.setUserName(user != null ? user.getUsername() : "Unknown");
                    dto.setComment(comment.getComment());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    /**
     * Преобразует сущность отзыва в ReviewReturnDto, добавляя данные о пользователе, университете и комментариях.
     */
    private ReviewReturnDto mapToReviewReturnDto(Review review) {
        ResponseEntity<UserDto> response = userClient.findUserById(review.getUserId());
        UserDto user = (response != null) ? response.getBody() : null;
        if (user == null) {
            log.warn("User not found for review id {} with userId {}", review.getId(), review.getUserId());
            user = new UserDto();
        }

        ReviewReturnDto dto = new ReviewReturnDto();
        dto.setId(review.getId());
        dto.setComment(review.getComment());
        dto.setRating(review.getRating());
        dto.setUserId(user.getId());
        dto.setUserName(user.getUsername());
        dto.setUniversityId(review.getUniversity().getId());
        dto.setUniversityName(review.getUniversity().getName());
        dto.setStatus(review.getStatus().name());
        dto.setLikes(review.getLikes());
        dto.setDislikes(review.getDislikes());
        // Получаем комментарии для отзыва
        List<ReviewCommentDto> comments = reviewCommentRepository.findByReview(review)
                .stream()
                .map(comment -> {
                    ReviewCommentDto commentDto = new ReviewCommentDto();
                    commentDto.setId(comment.getId());
                    commentDto.setUserId(comment.getUserId());
                    ResponseEntity<UserDto> userResponse = userClient.findUserById(comment.getUserId());
                    UserDto commentUser = (userResponse != null) ? userResponse.getBody() : null;
                    commentDto.setUserName(commentUser != null ? commentUser.getUsername() : "Unknown");
                    commentDto.setComment(comment.getComment());
                    return commentDto;
                })
                .collect(Collectors.toList());
        dto.setComments(comments);
        return dto;
    }

    private void saveReview(Review review) {
        reviewRepository.save(review);
    }

    private void saveReviewComment(ReviewComment reviewComment) {
        reviewCommentRepository.save(reviewComment);
    }
}