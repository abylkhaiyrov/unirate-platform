package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/specialties")
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

    @Operation(
            summary = "Search specialties by name",
            description = "Returns a list of specialties matching the provided name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of specialties successfully retrieved"),
                    @ApiResponse(responseCode = "400", description = "Invalid input provided"),
                    @ApiResponse(responseCode = "404", description = "No specialties found for the given name")
            }
    )
    @GetMapping("/search")
    public ResponseEntity<List<SpecialtyDto>> getSpecialtiesByName(
            @RequestParam String name) {
        List<SpecialtyDto> specialties = specialtyService.getSpecialityByName(name);
        if (specialties.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(specialties);
        }
        return ResponseEntity.ok(specialties);
    }

}