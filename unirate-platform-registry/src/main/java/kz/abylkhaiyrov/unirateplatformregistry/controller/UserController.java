package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.dto.ChangePasswordDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping(value = "/api/user", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User", description = "Endpoints for user management")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Get current user",
            description = "Returns information about the currently authenticated user"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved current user", content = @Content)
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(
            summary = "Get users list",
            description = "Returns a paginated list of users"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved list of users", content = @Content)
    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(
            summary = "Get user by ID",
            description = "Returns user data by the specified ID"
    )
    @ApiResponse(responseCode = "200", description = "Successfully retrieved user", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "Identifier of the user", required = true)
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Update user by ID",
            description = "Updates user data for the specified ID"
    )
    @ApiResponse(responseCode = "200", description = "User successfully updated", content = @Content)
    @PostMapping("/{id}/update")
    public ResponseEntity<UserDto> updateUserById(
            @Parameter(description = "Identifier of the user", required = true)
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto
    ) {
        return ResponseEntity.ok(userService.updateById(id, userDto));
    }

    @Operation(
            summary = "Update profile picture URL",
            description = "Updates the profile picture URL for the specified user ID"
    )
    @ApiResponse(responseCode = "200", description = "Profile picture URL successfully updated", content = @Content)
    @PutMapping("/{userId}/profile-url")
    public ResponseEntity<String> updateProfileUrl(
            @Parameter(description = "Identifier of the user", required = true)
            @PathVariable("userId") Long userId,
            @Parameter(description = "New profile picture URL", required = true)
            @RequestParam("url") String url
    ) {
        return ResponseEntity.ok(userService.updateUserProfile(userId, url));
    }

    @Operation(
            summary = "Change user password",
            description = "Allows an authenticated user to change their password by providing their current and new passwords"
    )
    @ApiResponse(responseCode = "200", description = "Password change successful", content = @Content)
    @PutMapping("/password")
    public ResponseEntity<Boolean> changePassword(
            @RequestBody @Valid ChangePasswordDto dto
    ) {
        boolean success = userService.changePassword(dto);
        return ResponseEntity.ok(success);
    }
}