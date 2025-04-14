package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReviewCommentDto {
    private Long id;
    private Long userId;
    private String userName;
    private String status;
    private String comment;
    private String profileImgUrl;
    private LocalDateTime createdAt;
}