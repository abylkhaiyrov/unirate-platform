package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UniversitySearchDto {
    private String searchFiled;
    private String city;
    private Long minTuition;
    private Long maxTuition;
    private Boolean hasMilitaryDepartment;
    private Boolean hasDormitory;
    private int rating = 10;
    private int page = 0;
    private int size = 10;
}
