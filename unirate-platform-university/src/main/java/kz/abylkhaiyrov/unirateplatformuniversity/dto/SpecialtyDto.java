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

    private List<Long> courseIds;

}
