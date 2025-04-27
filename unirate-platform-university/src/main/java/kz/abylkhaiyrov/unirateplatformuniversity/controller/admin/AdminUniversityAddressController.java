package kz.abylkhaiyrov.unirateplatformuniversity.controller.admin;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/university-address")
@Tag(name = "Admin University Address API", description = "Admin operations for managing university addresses")
@RequiredArgsConstructor
public class AdminUniversityAddressController {

    private final UniversityAddressService universityAddressService;

    @Operation(
            summary = "Create a new university address",
            description = "Creates a new university address and returns its data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address successfully created")
            }
    )
    @PostMapping
    public ResponseEntity<UniversityAddressDto> createAddress(@RequestBody UniversityAddressDto dto) {
        UniversityAddressDto created = universityAddressService.createAddress(dto);
        return ResponseEntity.ok(created);
    }

    @Operation(
            summary = "Update an existing university address",
            description = "Updates the university address by the given ID and returns the updated data",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address successfully updated"),
                    @ApiResponse(responseCode = "404", description = "University address not found")
            }
    )
    @PutMapping("/{id}")
    public ResponseEntity<UniversityAddressDto> updateAddress(
            @Parameter(description = "University address ID to be updated", required = true) @PathVariable Long id,
            @RequestBody UniversityAddressDto dto) {
        UniversityAddressDto updated = universityAddressService.updateAddress(id, dto);
        return ResponseEntity.ok(updated);
    }

    @Operation(
            summary = "Delete a university address",
            description = "Deletes the university address by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "University address not found")
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(
            @Parameter(description = "University address ID to be deleted", required = true) @PathVariable Long id) {
        universityAddressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}