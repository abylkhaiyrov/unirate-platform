package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/review")
@Tag(name = "Review API", description = "Operations to get reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Get review by ID",
            description = "Returns the review data with full information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review found"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @GetMapping("/{reviewId}")
    public ResponseEntity<ReviewReturnDto> getReviewById(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        ReviewReturnDto review = reviewService.getById(reviewId);
        return ResponseEntity.ok(review);
    }

    @Operation(
            summary = "Get all reviews",
            description = "Returns a paginated list of all reviews",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of reviews successfully retrieved")
            }
    )
    @GetMapping
    public ResponseEntity<Page<ReviewReturnDto>> getAllReviews(Pageable pageable) {
        Page<ReviewReturnDto> reviews = reviewService.getAll(pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Get reviews for a specific forum",
            description = "Returns a paginated list of reviews for the specified forum",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of reviews successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Forum not found")
            }
    )
    @GetMapping("/forum/{forumId}")
    public ResponseEntity<Page<ReviewReturnDto>> getReviewsByForum(
            @Parameter(description = "Forum ID", required = true) @PathVariable Long forumId,
            Pageable pageable) {
        Page<ReviewReturnDto> reviews = reviewService.getByForum(forumId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @Operation(
            summary = "Get review statistics for a university",
            description = "Returns aggregated review statistics (average rating and number of reviews) for the given university",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Statistics successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @GetMapping("/university/{universityId}/stats")
    public ResponseEntity<UniversityReviewStats> getUniversityReviewStats(
            @Parameter(description = "University ID", required = true) @PathVariable Long universityId) {
        UniversityReviewStats stats = reviewService.getStatsForUniversity(universityId);
        return ResponseEntity.ok(stats);
    }

    @Operation(
            summary = "Get comments for a review",
            description = "Returns a list of comments for the specified review",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of comments successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @GetMapping("/{reviewId}/comments")
    public ResponseEntity<List<ReviewCommentDto>> getComments(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        List<ReviewCommentDto> comments = reviewService.getComments(reviewId);
        return ResponseEntity.ok(comments);
    }
}