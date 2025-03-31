package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "User", description = "Endpoints для управления пользователями")
@Slf4j
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(
            summary = "Получить текущего пользователя",
            description = "Возвращает информацию о текущем аутентифицированном пользователе"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение текущего пользователя", content = @Content)
    @GetMapping("/current")
    public ResponseEntity<UserDto> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @Operation(
            summary = "Получить список пользователей",
            description = "Возвращает страницу пользователей с поддержкой пагинации"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение списка пользователей", content = @Content)
    @GetMapping
    public ResponseEntity<Page<UserDto>> getUsers(Pageable pageable) {
        return ResponseEntity.ok(userService.findAll(pageable));
    }

    @Operation(
            summary = "Получить пользователя по ID",
            description = "Возвращает данные пользователя по указанному идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Успешное получение пользователя", content = @Content)
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable Long id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @Operation(
            summary = "Обновить данные пользователя",
            description = "Обновляет данные пользователя по заданному идентификатору"
    )
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен", content = @Content)
    @PostMapping("/{id}/update")
    public ResponseEntity<UserDto> updateUserById(
            @Parameter(description = "Идентификатор пользователя", required = true)
            @PathVariable Long id,
            @RequestBody @Valid UserDto userDto) {
        return ResponseEntity.ok(userService.updateById(id, userDto));
    }
}