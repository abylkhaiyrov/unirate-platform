package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-api/university-address")
@Tag(name = "University Address API", description = "Operations to get university addresses")
@RequiredArgsConstructor
public class UniversityAddressController {

    private final UniversityAddressService universityAddressService;

    @Operation(
            summary = "Get university address by ID",
            description = "Returns the university address data by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Address found"),
                    @ApiResponse(responseCode = "404", description = "University address not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UniversityAddressDto> getAddressById(
            @Parameter(description = "University address ID", required = true) @PathVariable Long id) {
        UniversityAddressDto address = universityAddressService.getAddressById(id);
        return ResponseEntity.ok(address);
    }

    @Operation(
            summary = "Get university addresses",
            description = "Returns a list of university addresses with optional filtering by city and pagination",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Addresses successfully retrieved")
            }
    )
    @GetMapping
    public ResponseEntity<Page<UniversityAddressDto>> getAddresses(
            @Parameter(description = "Filter addresses by city", required = false) @RequestParam(required = false) String city,
            Pageable pageable) {
        Page<UniversityAddressDto> addresses = universityAddressService.getAddresses(pageable, city);
        return ResponseEntity.ok(addresses);
    }
}