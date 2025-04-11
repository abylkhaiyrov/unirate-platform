package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SpecialtyDto {

    private Long id;

    private String name;

    private String description;

    private Long facultyId;

    private List<CourseDto> courses;

    private String specialtyImageUrl;

    private String facultyName;

    private String universityName;

    private String gopCode;

    private String grants;

    private String minScores;

}
