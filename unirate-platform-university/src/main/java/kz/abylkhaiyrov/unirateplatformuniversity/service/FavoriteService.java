package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateFavoriteDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityFavDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Favorite;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.FavoriteRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityAddressRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UniversityRepository universityRepository;
    private final UniversityAdapter universityAdapter;
    private final UniversityAddressRepository universityAddressRepository;
    private final FacultyService facultyService;

    public UniversityFavDto createFavorite(CreateFavoriteDto dto) {
        log.info("Create favorite with dto: {}", dto.toString());
        var university = universityRepository.findById(dto.getUniversityId()).orElseThrow(() -> new NotFoundException("University not found with id: " + dto.getUniversityId()));
        var universityAddress = universityAddressRepository.findByUniversityId(university.getId()).orElse(null);
        var faculty = facultyService.getAllFacultiesByUniversityId(university.getId());
        var entity = new Favorite();
        entity.setUniversity(university);
        entity.setUserId(dto.getUserId());
        entity = save(entity);
        return universityAdapter.entity2DtoFromFav(entity, university, universityAddress, faculty);
    }

    private Favorite save(Favorite favorite) {
        return favoriteRepository.save(favorite);
    }

    public List<UniversityFavDto> getAllByUserId(Long userId) {
        log.info("Get all favorites by userId: {}", userId);
        return favoriteRepository.findAllByUserId(userId)
                .stream()
                .map(favorite -> {
                    var university = universityRepository.findById(favorite.getUniversity().getId())
                            .orElseThrow(() -> new NotFoundException("University not found with id: "
                                    + favorite.getUniversity().getId()));
                    var universityAddress = universityAddressRepository.findByUniversityId(university.getId())
                            .orElse(null);
                    var faculty = facultyService.getAllFacultiesByUniversityId(university.getId());
                    return universityAdapter.entity2DtoFromFav(favorite, university, universityAddress, faculty);
                })
                .collect(Collectors.toList());
    }

    public void deleteById(Long id) {
        log.info("Delete favorite by id: {}", id);
        favoriteRepository.deleteById(id);
    }

}
