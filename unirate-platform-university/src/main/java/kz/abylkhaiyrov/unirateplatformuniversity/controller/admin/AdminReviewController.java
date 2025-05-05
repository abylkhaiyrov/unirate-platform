package kz.abylkhaiyrov.unirateplatformuniversity.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.*;
import kz.abylkhaiyrov.unirateplatformuniversity.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/review")
@Tag(name = "Admin Review API", description = "Admin operations for managing reviews")
@RequiredArgsConstructor
public class AdminReviewController {

    private final ReviewService reviewService;

    @Operation(
            summary = "Create a new review",
            description = "Creates a new review and returns its data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review successfully created")
            }
    )
    @PostMapping
    public ResponseEntity<ReviewReturnDto> createReview(@RequestBody CreateReviewDto reviewDto) {
        var createdReview = reviewService.create(reviewDto);
        return ResponseEntity.ok(createdReview);
    }

    @Operation(
            summary = "Update an existing review",
            description = "Updates the data of an existing review",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewReturnDto> updateReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId,
            @RequestBody ReviewDto reviewDto) {
        var updatedReview = reviewService.update(reviewId, reviewDto);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(
            summary = "Delete a review",
            description = "Deletes a review by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        reviewService.delete(reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(
            summary = "Delete a review comment by ID",
            description = "Deletes the review comment by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review comment successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Review comment not found")
            }
    )
    @DeleteMapping("/{reviewCommentId}")
    public ResponseEntity<Void> deleteReviewComment(
            @Parameter(description = "Review comment ID", required = true) @PathVariable Long reviewCommentId) {
        reviewService.deleteReviewComment(reviewCommentId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Moderate a review",
            description = "Moderates a review by setting its status to APPROVED or REJECTED",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review successfully moderated"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @PutMapping("/{reviewId}/moderate")
    public ResponseEntity<ReviewReturnDto> moderateReview(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId,
            @Parameter(description = "Approve review", required = true) @RequestParam boolean approve) {
        var moderatedReview = reviewService.moderate(reviewId, approve);
        return ResponseEntity.ok(moderatedReview);
    }

    @Operation(
            summary = "Add a like to a review",
            description = "Adds a like to a review and returns updated data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Like successfully added"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @PostMapping("/{reviewId}/like")
    public ResponseEntity<ReviewReturnDto> addLike(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        ReviewReturnDto updatedReview = reviewService.addLike(reviewId);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(
            summary = "Add a dislike to a review",
            description = "Adds a dislike to a review and returns updated data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Dislike successfully added"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @PostMapping("/{reviewId}/dislike")
    public ResponseEntity<ReviewReturnDto> addDislike(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId) {
        ReviewReturnDto updatedReview = reviewService.addDislike(reviewId);
        return ResponseEntity.ok(updatedReview);
    }

    @Operation(
            summary = "Add a comment to a review",
            description = "Adds a comment to a review and returns the created comment data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comment successfully added"),
                    @ApiResponse(responseCode = "404", description = "Review not found")
            }
    )
    @PostMapping("/{reviewId}/comments")
    public ResponseEntity<ReviewCommentDto> addComment(
            @Parameter(description = "Review ID", required = true) @PathVariable Long reviewId,
            @RequestBody ReviewCommentDto commentDto) {
        ReviewCommentDto createdComment = reviewService.addComment(reviewId, commentDto);
        return ResponseEntity.ok(createdComment);
    }

    @Operation(
            summary = "Delete reviews and their comments",
            description = "Deletes reviews and their associated comments by the given review IDs",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews and comments successfully deleted"),
                    @ApiResponse(responseCode = "400", description = "Invalid input provided"),
                    @ApiResponse(responseCode = "404", description = "Reviews not found for the given IDs"),
                    @ApiResponse(responseCode = "500", description = "Internal server error")
            }
    )
    @DeleteMapping("/reviews")
    public ResponseEntity<Void> deleteReviewsWithIds(
            @RequestParam List<Long> reviewIds) {
        try {
            reviewService.deleteReviewsWithIds(reviewIds);
            return ResponseEntity.ok().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}