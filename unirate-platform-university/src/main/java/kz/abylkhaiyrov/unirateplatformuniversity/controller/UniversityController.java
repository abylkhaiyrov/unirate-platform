package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversitySearchDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/university")
@Tag(name = "University API", description = "Operations to get universities")
@RequiredArgsConstructor
public class UniversityController {

    private final UniversityService universityService;

    @Operation(
            summary = "Get university by ID",
            description = "Returns university data by the given ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University found"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @GetMapping("/{id}")
    public ResponseEntity<UniversityDto> getUniversityById(
            @Parameter(description = "University ID", required = true) @PathVariable Long id) {
        var university = universityService.getById(id);
        return ResponseEntity.ok(university);
    }

    @Operation(
            summary = "Get university by name",
            description = "Returns university data by its name",
            responses = {
                    @ApiResponse(responseCode = "200", description = "University found"),
                    @ApiResponse(responseCode = "404", description = "University not found")
            }
    )
    @GetMapping("/name/{name}")
    public ResponseEntity<UniversityDto> getUniversityByName(
            @Parameter(description = "University name", required = true) @PathVariable String name) {
        var university = universityService.getUniversityByName(name);
        return ResponseEntity.ok(university);
    }

    @Operation(
            summary = "Get all universities",
            description = "Returns a list of all universities",
            responses = {
                    @ApiResponse(responseCode = "200", description = "List of universities successfully retrieved")
            }
    )
    @GetMapping
    public ResponseEntity<List<UniversityDto>> getAllUniversities() {
        var universities = universityService.getAllUniversities();
        return ResponseEntity.ok(universities);
    }

    @Operation(
            summary = "Get universities with pagination",
            description = "Returns a paginated list of universities based on the provided parameters",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Page of universities successfully retrieved")
            }
    )
    @GetMapping("/page")
    public ResponseEntity<Page<UniversityDto>> getUniversitiesByPage(Pageable pageable) {
        var universities = universityService.getUniversitiesByPage(pageable);
        return ResponseEntity.ok(universities);
    }

    @Operation(
            summary = "Search universities",
            description = "Searches for universities by keyword in the name or description",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Search results successfully retrieved")
            }
    )
    @PostMapping("/search")
    public ResponseEntity<Page<UniversityDto>> searchUniversities(@RequestBody UniversitySearchDto universitySearchDto) {
        var results = universityService.searchUniversities(universitySearchDto);
        return ResponseEntity.ok(results);
    }

    @Operation(
            summary = "Get top universities",
            description = "Returns a list of top universities sorted by rating",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Top universities successfully retrieved")
            }
    )
    @GetMapping("/top")
    public ResponseEntity<List<UniversityDto>> getTopUniversities(
            @Parameter(description = "Limit the number of top universities", required = true) @RequestParam Integer limit) {
        var topUniversities = universityService.getTopUniversities(limit);
        return ResponseEntity.ok(topUniversities);
    }
}