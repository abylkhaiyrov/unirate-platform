package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseDto {

    private String name;

    private String description;

    private Integer durationYears;

    private String language;

    private Long tuitionFee;

    private String studyMode;

    private String requirements;

    private Long universityId;
}
