package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateReviewDto {

    private Long forumId;

    private Long userId;

    private String comment;

    private Short rating;

}
