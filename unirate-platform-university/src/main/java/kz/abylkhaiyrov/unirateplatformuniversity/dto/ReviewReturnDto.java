package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class ReviewReturnDto {
    private Long id;
    private Long universityId;
    private String universityName;
    private String comment;
    private Short rating;
    private Long userId;
    private String userName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private String status;
    private Integer likes;
    private Integer dislikes;
    private List<String> attachments;
    // Список комментариев к отзыву
    private List<ReviewCommentDto> comments;
}