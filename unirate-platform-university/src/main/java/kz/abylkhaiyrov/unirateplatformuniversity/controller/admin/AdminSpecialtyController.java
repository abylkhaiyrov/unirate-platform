package kz.abylkhaiyrov.unirateplatformuniversity.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateSpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/specialty")
@Tag(name = "Admin Specialty API", description = "Admin operations for managing specialties")
@RequiredArgsConstructor
public class AdminSpecialtyController {

    private final SpecialtyService specialtyService;

    @Operation(
            summary = "Create a new specialty",
            description = "Creates a new specialty and returns the created data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialty successfully created")
            }
    )
    @PostMapping
    public ResponseEntity<SpecialtyDto> createSpecialty(@RequestBody CreateSpecialtyDto specialtyDto) {
        var created = specialtyService.createSpecialty(specialtyDto);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Update an existing specialty",
            description = "Updates the data of an existing specialty",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialty successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Specialty not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDto> updateSpecialty(@PathVariable Long id,
                                                        @RequestBody SpecialtyDto specialtyDto) {
        var updated = specialtyService.updateSpecialty(id, specialtyDto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a specialty",
            description = "Deletes the specialty by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Specialty successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Specialty not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Update profile URL for a specialty",
            description = "Updates the profile URL for the given specialty",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Profile URL successfully updated")
            }
    )
    @PutMapping("/{specialtyId}/update")
    public ResponseEntity<String> updateProfileUrl(
            @PathVariable("specialtyId") Long specialtyId,
            @RequestParam("url") String url) {
        return ResponseEntity.ok(specialtyService.updateSpecialtyProfile(specialtyId, url));
    }
}