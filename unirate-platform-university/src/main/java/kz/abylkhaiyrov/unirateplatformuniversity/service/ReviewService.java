package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.client.UserClient;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.ReviewComment;
import kz.abylkhaiyrov.unirateplatformuniversity.enums.ReviewStatus;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ForumRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ReviewRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.ReviewCommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ReviewCommentRepository reviewCommentRepository;
    private final UserClient userClient;
    private final ForumRepository forumRepository;

    /**
     * Создаёт новый отзыв.
     * Поля createdAt, updatedAt, статус, лайки и вложения устанавливаются по умолчанию.
     */
    public ReviewReturnDto create(CreateReviewDto dto) {
        Review review = new Review();
        review.setComment(dto.getComment());
        review.setRating(dto.getRating());
        var forum = forumRepository.findById(dto.getForumId()).orElseThrow(() -> new NotFoundException("Forum not found with forumId: " + dto.getForumId()));
        review.setForum(forum);
        review.setUserId(dto.getUserId());
        review = saveReview(review);
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
        review = saveReview(review);
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
        review = saveReview(review);
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
     * Возвращает отзывы для конкретного форума с возможностью сортировки и фильтрации.
     */
    public Page<ReviewReturnDto> getByForum(Long forumId, Pageable pageable) {
        var forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found with forumId: " + forumId));
        return reviewRepository.findByForum(forum, pageable)
                .map(this::mapToReviewReturnDto);
    }

    /**
     * Добавляет лайк к отзыву.
     */
    public ReviewReturnDto addLike(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setLikes(review.getLikes() + 1);
        review = saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Добавляет дизлайк к отзыву.
     */
    public ReviewReturnDto addDislike(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new NotFoundException("Review not found with id: " + reviewId));
        review.setDislikes(review.getDislikes() + 1);
        review = saveReview(review);
        return mapToReviewReturnDto(review);
    }

    /**
     * Возвращает агрегированную статистику для университета: средний рейтинг и количество отзывов.
     */
    public UniversityReviewStats getStatsForUniversity(Long forumId) {
        var forum = forumRepository.findById(forumId).orElseThrow(() -> new NotFoundException("Forum not found with forumId: " + forumId));
        List<Review> reviews = reviewRepository.findByForum(forum);
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
        comment = saveReviewComment(comment);
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
        dto.setForumId(review.getForum().getId());
        dto.setForumName(review.getForum().getName());
        dto.setStatus(review.getStatus().name());
        dto.setLikes(review.getLikes());
        dto.setDislikes(review.getDislikes());
        dto.setCreatedAt(LocalDateTime.ofInstant(review.getCreatedDate(), ZoneId.systemDefault()));
        dto.setUpdatedAt(LocalDateTime.ofInstant(review.getLastModifiedDate(), ZoneId.systemDefault()));
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
                    commentDto.setCreatedAt(LocalDateTime.ofInstant(comment.getCreatedDate(), ZoneId.systemDefault()));
                    return commentDto;
                })
                .collect(Collectors.toList());
        dto.setComments(comments);
        return dto;
    }

    private Review saveReview(Review review) {
        return  reviewRepository.save(review);
    }

    private ReviewComment saveReviewComment(ReviewComment reviewComment) {
        return reviewCommentRepository.save(reviewComment);
    }
}