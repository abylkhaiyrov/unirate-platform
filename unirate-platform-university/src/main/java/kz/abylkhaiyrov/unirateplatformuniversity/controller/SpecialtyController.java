package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/specialty")
@Tag(name = "Specialty API", description = "Operations to get specialties")
@RequiredArgsConstructor
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @Operation(
            summary = "Get all specialties",
            description = "Returns a list of all specialties",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of specialties successfully retrieved")
            }
    )
    @GetMapping
    public ResponseEntity<List<SpecialtyDto>> getAllSpecialties() {
        List<SpecialtyDto> specialties = specialtyService.getAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    @Operation(
            summary = "Get specialty by ID",
            description = "Returns data of a specialty by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialty found"),
                    @ApiResponse(responseCode = "404", description = "Specialty not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDto> getSpecialtyById(@PathVariable Long id) {
        SpecialtyDto specialty = specialtyService.getSpecialtyById(id);
        return ResponseEntity.ok(specialty);
    }
}