package kz.abylkhaiyrov.unirateplatformuniversity.dto;

import lombok.Data;

import java.util.List;

@Data
public class FacultyAndSpecialityDto {
    private FacultyDto facultyDto;
    private List<SpecialtyDto> specialtyDtos;
}

