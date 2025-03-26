package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ForumDto {

    private Long id;

    private String name;

    private String description;

    private Long universityId;

    private List<ReviewReturnDto> topReviews;
}
