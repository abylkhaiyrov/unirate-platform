package kz.abylkhaiyrov.unirateplatformuniversity.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateUniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/university")
@Tag(name = "Admin University API", description = "Admin operations for managing universities")
@RequiredArgsConstructor
public class AdminUniversityController {

    private final UniversityService universityService;

    @Operation(
            summary = "Create a new university",
            description = "Creates a new university and returns its data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University successfully created")
            }
    )
    @PostMapping
    public ResponseEntity<UniversityDto> createUniversity(@RequestBody CreateUniversityDto universityDto) {
        UniversityDto created = universityService.createUniversity(universityDto);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Update an existing university",
            description = "Updates the data of an existing university",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University successfully updated"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UniversityDto> updateUniversity(
            @Parameter(description = "University ID", required = true) @PathVariable Long id,
            @RequestBody CreateUniversityDto universityDto) {
        UniversityDto updated = universityService.updateUniversity(id, universityDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete (deactivate) a university",
            description = "Deactivates the university by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University successfully deactivated"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUniversity(@Parameter(description = "University ID", required = true) @PathVariable Long id) {
        universityService.deleteUniversity(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update university logo URL",
            description = "Updates the logo URL of the university",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Logo successfully updated"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @PutMapping("/{id}/logo")
    public ResponseEntity<UniversityDto> updateLogo(
            @Parameter(description = "University ID", required = true) @PathVariable Long id,
            @Parameter(description = "New logo URL", required = true) @RequestParam String logoUrl) {
        UniversityDto updated = universityService.updateLogo(id, logoUrl);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Activate or deactivate university",
            description = "Sets the active status of the university",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University status successfully updated"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @PutMapping("/{id}/active")
    public ResponseEntity<UniversityDto> activateUniversity(
            @Parameter(description = "University ID", required = true) @PathVariable Long id,
            @Parameter(description = "Active status", required = true) @RequestParam Boolean active) {
        UniversityDto updated = universityService.activateUniversity(id, active);
        return ResponseEntity.ok(updated);
    }
}