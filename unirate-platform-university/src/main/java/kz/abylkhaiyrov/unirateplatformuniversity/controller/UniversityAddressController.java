package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.UniversityAddressService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/open-api/university-addresses")
@RequiredArgsConstructor
@Tag(name = "University Address API", description = "Операции для управления адресами университетов")
public class UniversityAddressController {

    private final UniversityAddressService universityAddressService;

    @Operation(
            summary = "Создание нового адреса университета",
            description = "Создаёт новый адрес университета и возвращает его данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Адрес успешно создан", content = @Content(mediaType = "application/json"))
            }
    )
    @PostMapping
    public UniversityAddressDto createAddress(@RequestBody UniversityAddressDto dto) {
        return universityAddressService.createAddress(dto);
    }

    @Operation(
            summary = "Получение адреса университета по id",
            description = "Возвращает данные адреса университета по указанному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Адрес успешно найден", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Адрес университета не найден", content = @Content)
            }
    )
    @GetMapping("/{id}")
    public UniversityAddressDto getAddressById(
            @Parameter(description = "Идентификатор адреса университета", required = true)
            @PathVariable Long id) {
        return universityAddressService.getAddressById(id);
    }

    @Operation(
            summary = "Получение списка адресов университетов",
            description = "Возвращает список адресов университетов с возможностью фильтрации по городу и с пагинацией",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Адреса успешно получены", content = @Content(mediaType = "application/json"))
            }
    )
    @GetMapping
    public Page<UniversityAddressDto> getAddresses(
            @Parameter(description = "Фильтрация адресов по городу", required = false)
            @RequestParam(required = false) String city,
            Pageable pageable) {
        return universityAddressService.getAddresses(pageable, city);
    }

    @Operation(
            summary = "Обновление адреса университета",
            description = "Обновляет данные адреса университета по указанному идентификатору и возвращает обновленные данные",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Адрес успешно обновлен", content = @Content(mediaType = "application/json")),
                    @ApiResponse(responseCode = "404", description = "Адрес университета не найден", content = @Content)
            }
    )
    @PutMapping("/{id}")
    public UniversityAddressDto updateAddress(
            @Parameter(description = "Идентификатор адреса университета для обновления", required = true)
            @PathVariable Long id,
            @RequestBody UniversityAddressDto dto) {
        return universityAddressService.updateAddress(id, dto);
    }

    @Operation(
            summary = "Удаление адреса университета",
            description = "Удаляет адрес университета по указанному идентификатору",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Адрес успешно удален", content = @Content),
                    @ApiResponse(responseCode = "404", description = "Адрес университета не найден", content = @Content)
            }
    )
    @DeleteMapping("/{id}")
    public void deleteAddress(
            @Parameter(description = "Идентификатор адреса университета для удаления", required = true)
            @PathVariable Long id) {
        universityAddressService.deleteAddress(id);
    }
}