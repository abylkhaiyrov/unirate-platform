package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class CreateSpecialtyDto {

    private String name;

    private String description;

    private Long facultyId;

    private String facultyName;

    private String universityName;

    private String gopCode;

    private String grants;

    private String minScores;

    private List<Long> courseIds;

}
