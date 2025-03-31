package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import kz.abylkhaiyrov.unirateplatformuniversity.enums.CommonName;
import lombok.Getter;

import java.util.List;

@Getter
public class UniversityComparisonRequestDto {

    private Long userId;

    private List<String> universityName;

    private List<CommonName> commonName;

}