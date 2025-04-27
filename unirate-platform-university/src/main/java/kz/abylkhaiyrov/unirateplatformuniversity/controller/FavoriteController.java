package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateFavoriteDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonRequestDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityFavDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favorites")
@Tag(name = "Favorite API", description = "Operations for managing favorite universities")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @Operation(
            summary = "Create a new favorite university",
            description = "Creates a new favorite university entry and returns the university data with additional information",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Favorite successfully created", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public ResponseEntity<UniversityFavDto> createFavorite(@RequestBody CreateFavoriteDto createFavoriteDto) {
        var favorite = favoriteService.createFavorite(createFavoriteDto);
        return ResponseEntity.ok(favorite);
    }

    @Operation(
            summary = "Get all favorites by user ID",
            description = "Returns a list of favorite universities for the given user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Favorites found", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Favorites not found", content = @Content)
            }
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<UniversityFavDto>> getAllFavoritesByUserId(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        var favorites = favoriteService.getAllByUserId(userId);
        if (favorites.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(favorites);
    }

    @Operation(
            summary = "Delete a favorite university entry",
            description = "Deletes the favorite university entry by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Favorite successfully deleted", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Favorite not found", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFavorite(
            @Parameter(description = "Favorite ID", required = true) @PathVariable Long id) {
        favoriteService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Search for university comparison",
            description = "Returns a list of university comparisons based on the given criteria",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Comparison successfully completed", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping("/comparison/filter")
    public ResponseEntity<List<UniversityComparisonDto>> searchUniversityComparison(
            @RequestBody UniversityComparisonRequestDto requestDto) {
        var comparisons = favoriteService.searchUniversityComparison(requestDto);
        return ResponseEntity.ok(comparisons);
    }
}