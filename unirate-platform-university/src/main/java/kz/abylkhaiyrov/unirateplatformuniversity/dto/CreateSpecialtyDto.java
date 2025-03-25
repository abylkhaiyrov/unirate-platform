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

    private List<Long> courseIds;

}
