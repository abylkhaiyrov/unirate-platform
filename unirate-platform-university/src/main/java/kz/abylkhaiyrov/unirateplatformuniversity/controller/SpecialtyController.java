package kz.abylkhaiyrov.unirateplatformuniversity.controller;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateSpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.SpecialtyDto;
import kz.abylkhaiyrov.unirateplatformuniversity.service.SpecialtyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/open-api/specialties")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyController {

    private final SpecialtyService specialtyService;

    @GetMapping
    public ResponseEntity<List<SpecialtyDto>> getAllSpecialties() {
        List<SpecialtyDto> specialties = specialtyService.getAllSpecialties();
        return ResponseEntity.ok(specialties);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SpecialtyDto> getSpecialtyById(@PathVariable Long id) {
        SpecialtyDto specialty = specialtyService.getSpecialtyById(id);
        return ResponseEntity.ok(specialty);
    }

    @PostMapping
    public ResponseEntity<SpecialtyDto> createSpecialty(@RequestBody CreateSpecialtyDto specialtyDto) {
        SpecialtyDto created = specialtyService.createSpecialty(specialtyDto);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SpecialtyDto> updateSpecialty(@PathVariable Long id,
                                                        @RequestBody SpecialtyDto specialtyDto) {
        SpecialtyDto updated = specialtyService.updateSpecialty(id, specialtyDto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSpecialty(@PathVariable Long id) {
        specialtyService.deleteSpecialty(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{specialtyId}/update")
    public ResponseEntity<String> updateProfileUrl(
            @PathVariable("specialtyId") Long specialtyId,
            @RequestParam("url") String url) {
        return ResponseEntity.ok(specialtyService.updateSpecialtyProfile(specialtyId, url));
    }

}