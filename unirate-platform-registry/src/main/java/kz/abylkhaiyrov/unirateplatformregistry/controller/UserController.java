package kz.abylkhaiyrov.unirateplatformregistry.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserUpdateDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserService;
import kz.abylkhaiyrov.unirateplatformregistry.util.Constans;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Tag(name = "User Controller", description = "API для управления пользователями")
@RestController
@RequiredArgsConstructor
@RequestMapping(path = Constans.API + Constans.VERSION_1 + Constans.USER_API, produces = "application/json")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить всех пользователей", description = "Возвращает список всех пользователей")
    @ApiResponse(responseCode = "200", description = "Список пользователей", content = @Content(mediaType = "application/json"))
    @GetMapping(path = "/get-all-users")
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @Operation(summary = "Получить пользователя по ID", description = "Возвращает данные пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json"))
    @GetMapping(path = "/get/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userService.getUserById(id));
    }

    @Operation(summary = "Получить пользователя по имени", description = "Возвращает данные пользователя по его имени пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json"))
    @GetMapping(path = "/get/username/{username}")
    public ResponseEntity<UserDto> getUserByUserName(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token, @PathVariable String username) {
        return ResponseEntity.ok(userService.getUserByUsername(token, username));
    }

    @Operation(summary = "Получить пользователя по email", description = "Возвращает данные пользователя по его email")
    @ApiResponse(responseCode = "200", description = "Пользователь найден", content = @Content(mediaType = "application/json"))
    @GetMapping(path = "/get/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token, @PathVariable String email) {
        return ResponseEntity.ok(userService.getUserByEmail(token, email));
    }

    @Operation(summary = "Удалить пользователя по ID", description = "Удаляет пользователя по его ID")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно удален", content = @Content)
    @ApiResponse(responseCode = "404", description = "Пользователь с таким ID не найден", content = @Content)
    @DeleteMapping(path = "/delete/by-id/{id}")
    public ResponseEntity<Void> deleteById(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token, @PathVariable Long id) {
        userService.deleteUserById(token, id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователя по имени", description = "Удаляет пользователя по его имени пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно удален", content = @Content)
    @ApiResponse(responseCode = "404", description = "Пользователь с таким именем не найден", content = @Content)
    @DeleteMapping(path = "/delete/by-username/{username}")
    public ResponseEntity<Void> deleteByUserName(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token, @PathVariable String username) {
        userService.deleteUserByUsername(token, username);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Удалить пользователя по email", description = "Удаляет пользователя по его email")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно удален", content = @Content)
    @ApiResponse(responseCode = "404", description = "Пользователь с таким email не найден", content = @Content)
    @DeleteMapping(path = "/delete/by-email/{email}")
    public ResponseEntity<Void> deleteByEmail(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token, @PathVariable String email) {
        userService.deleteUserByEmail(token, email);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Обновить данные пользователя", description = "Обновляет данные существующего пользователя")
    @ApiResponse(responseCode = "200", description = "Пользователь успешно обновлен", content = @Content(mediaType = "application/json"))
    @ApiResponse(responseCode = "404", description = "Пользователь не найден", content = @Content)
    @PatchMapping(path = "/update")
    public ResponseEntity<UserDto> updateByUser(@Parameter(hidden = true) @RequestHeader(AUTHORIZATION) String token,@RequestParam Long userId , @RequestBody UserUpdateDto dto) {
        return ResponseEntity.ok(userService.updateUser(token,userId, dto));
    }
}