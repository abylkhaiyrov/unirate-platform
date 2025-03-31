package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateFavoriteDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityComparisonRequestDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityFavDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Favorite;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.FavoriteRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityAddressRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.criteria.*;
import java.util.ArrayList;
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
    private final EntityManager entityManager;

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

    public List<UniversityComparisonDto> searchUniversityComparison(UniversityComparisonRequestDto requestDto) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<UniversityComparisonDto> cq = cb.createQuery(UniversityComparisonDto.class);

        // Корневая сущность – Favorite
        Root<Favorite> favoriteRoot = cq.from(Favorite.class);

        // LEFT JOIN с университетом
        Join<Favorite, University> universityJoin = favoriteRoot.join("universities", JoinType.LEFT);
        // LEFT JOIN с факультетом университета
        Join<University, Faculty> facultyJoin = universityJoin.join("faculties", JoinType.LEFT);

        List<Predicate> predicates = new ArrayList<>();

        // Фильтр по userId
        predicates.add(cb.equal(favoriteRoot.get("userId"), requestDto.getUserId()));

        // Фильтр по именам университетов (если переданы)
        if (requestDto.getUniversityName() != null && !requestDto.getUniversityName().isEmpty()) {
            predicates.add(universityJoin.get("name").in(requestDto.getUniversityName()));
        }

        // Фильтр по commonName факультета (если переданы)
        if (requestDto.getCommonName() != null && !requestDto.getCommonName().isEmpty()) {
            predicates.add(facultyJoin.get("commonName").in(requestDto.getCommonName()));
        }

        // Формируем проекцию в DTO с нужными полями
        cq.select(cb.construct(
                        UniversityComparisonDto.class,
                        universityJoin.get("name"),              // universityName
                        universityJoin.get("rating"),            // rating
                        facultyJoin.get("baseCost"),             // baseCost
                        universityJoin.get("militaryDepartment"),// militaryDepartment
                        universityJoin.get("dormitory"),         // dormitory
                        facultyJoin.get("name")                  // facultyName
                ))
                .where(cb.and(predicates.toArray(new Predicate[0])))
                .distinct(true);

        return entityManager.createQuery(cq).getResultList();
    }

}
