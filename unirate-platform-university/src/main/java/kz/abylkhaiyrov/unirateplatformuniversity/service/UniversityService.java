package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.CreateUniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversitySearchDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.EmptyException;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static kz.abylkhaiyrov.unirateplatformuniversity.util.StringUtils.lowerSqlLike;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityAdapter adapter;
    private final UniversityAddressService universityAddressService;
    private final FacultyService facultyService;

    public UniversityDto createUniversity(CreateUniversityDto dto){
        log.info("Creating university with DTO: {}", dto);
        var entity = new University();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setContactEmail(dto.getContactEmail());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setLocation(dto.getLocation());
        entity.setWebsite(dto.getWebsite());
        entity.setBaseCost(dto.getBaseCost());
        entity.setRating(BigDecimal.ZERO);
        entity.setRatingCount(dto.getRatingCount());
        entity.setMilitaryDepartment(dto.getMilitaryDepartment());
        entity.setDormitory(dto.getDormitory());
        entity = saveUniversity(entity);
        log.info("University created with ID: {}", entity.getId());
        return adapter.entity2Dto(entity);
    }

    private University saveUniversity(University entity){
        return universityRepository.save(entity);
    }

    public UniversityDto getUniversityByName(String name){
        if (name == null){
            throw new EmptyException("University name cannot be null");
        }

        var entity = universityRepository.findByNameContainingIgnoreCase(name)
                .orElseThrow(() -> new NotFoundException("University not found with name: " + name));
        var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
        var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
        return adapter.entity2Dto(entity, universityAddress, faculty);
    }

    public University getUniversityById(Long id){
        if (id == null){
            throw new EmptyException("University id cannot be null");
        }

        return universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
    }

    @Transactional
    public void plusRating(Integer newRating, Long universityId) {
        if (newRating < 1) {
            newRating = 1;
        } else if (newRating > 5) {
            newRating = 5;
        }

        var entity = universityRepository.findById(universityId)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + universityId));

        var oldAvg = entity.getRating();
        var oldCount = entity.getRatingCount();

        BigDecimal updatedAvg;
        if (oldCount == 0 || oldAvg.compareTo(BigDecimal.ZERO) == 0) {
            updatedAvg = BigDecimal.valueOf(newRating);
            oldCount = 0L;
        } else {
            updatedAvg = oldAvg.multiply(BigDecimal.valueOf(oldCount))
                    .add(BigDecimal.valueOf(newRating))
                    .divide(BigDecimal.valueOf(oldCount + 1), 2, RoundingMode.HALF_UP);
        }

        if (updatedAvg.compareTo(BigDecimal.ONE) < 0) {
            updatedAvg = BigDecimal.ONE;
        } else if (updatedAvg.compareTo(BigDecimal.valueOf(5)) > 0) {
            updatedAvg = BigDecimal.valueOf(5);
        }

        entity.setRating(updatedAvg);
        entity.setRatingCount(oldCount + 1);
        saveUniversity(entity);
    }

    public List<UniversityDto> getAllUniversities() {
        var list = universityRepository.findAll();
        return list.stream()
                .map(entity -> {
                    var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
                    var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
                    return adapter.entity2Dto(entity, universityAddress, faculty);
                })
                .collect(Collectors.toList());
    }

    public Page<UniversityDto> getUniversitiesByPage(Pageable pageable) {
        return universityRepository.findAllByActiveTrue(pageable)
                .map(entity -> {
                    var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
                    var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
                    return adapter.entity2Dto(entity, universityAddress, faculty);
                });
    }

    public UniversityDto updateUniversity(Long id, CreateUniversityDto dto) {
        log.info("Updating university with id: {} and data: {}", id, dto);
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        if (Objects.nonNull(dto.getName()) && (entity.getName() == null ||
                !entity.getName().equals(dto.getName()))) {
            entity.setName(dto.getName());
        }
        if (Objects.nonNull(dto.getDescription()) && (entity.getDescription() == null ||
                !entity.getDescription().equals(dto.getDescription()))) {
            entity.setDescription(dto.getDescription());
        }
        if (Objects.nonNull(dto.getContactEmail()) && (entity.getContactEmail() == null ||
                !entity.getContactEmail().equals(dto.getContactEmail()))) {
            entity.setContactEmail(dto.getContactEmail());
        }
        if (Objects.nonNull(dto.getLogoUrl()) && (entity.getLogoUrl() == null ||
                !entity.getLogoUrl().equals(dto.getLogoUrl()))) {
            entity.setLogoUrl(dto.getLogoUrl());
        }
        if (Objects.nonNull(dto.getLocation()) && (entity.getLocation() == null ||
                !entity.getLocation().equals(dto.getLocation()))) {
            entity.setLocation(dto.getLocation());
        }
        if (Objects.nonNull(dto.getWebsite()) && (entity.getWebsite() == null ||
                !entity.getWebsite().equals(dto.getWebsite()))) {
            entity.setWebsite(dto.getWebsite());
        }
        if (Objects.nonNull(dto.getBaseCost()) && (entity.getBaseCost() == null ||
                !entity.getBaseCost().equals(dto.getBaseCost()))) {
            entity.setBaseCost(dto.getBaseCost());
        }
        saveUniversity(entity);
        log.info("University updated: {}", entity);
        var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
        var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
        return adapter.entity2Dto(entity, universityAddress, faculty);
    }

    public void deleteUniversity(Long id) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Deleting (deactivating) university with id: {}", id);
        entity.setActive(false);
        saveUniversity(entity);
    }

    public Page<UniversityDto> searchUniversities(UniversitySearchDto searchDto) {
        log.info("Searching universities with criteria: searchField={}, city={}, tuition range={} to {}, military department={}, dormitory={}, rating={}",
                searchDto.getSearchFiled(), searchDto.getCity(), searchDto.getMinTuition(), searchDto.getMaxTuition(),
                searchDto.getHasMilitaryDepartment(), searchDto.getHasDormitory(), searchDto.getRating());

        Specification<University> spec = Specification.where(null);

        if (searchDto.getSearchFiled() != null && !searchDto.getSearchFiled().isEmpty()) {
            spec = spec.and((root, query, builder) ->
                    builder.or(
                            builder.like(builder.lower(root.get("name")), "%" + searchDto.getSearchFiled().toLowerCase() + "%"),
                            builder.like(builder.lower(root.get("description")), "%" + searchDto.getSearchFiled().toLowerCase() + "%")
                    )
            );
        }
        if (StringUtils.isNotEmpty(searchDto.getCity())) {
            var city = searchDto.getCity();
            spec = spec.and((root, query, builder) -> {
                var addressJoin = root.join("universityAddress", JoinType.LEFT);
                return builder.like(builder.lower(addressJoin.get("city")), lowerSqlLike(city));
            });
        }
        if (searchDto.getMinTuition() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.ge(root.get("baseCost"), searchDto.getMinTuition()));
        }
        if (searchDto.getMaxTuition() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.le(root.get("baseCost"), searchDto.getMaxTuition()));
        }

        if (searchDto.getHasMilitaryDepartment() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("militaryDepartment"), searchDto.getHasMilitaryDepartment()));
        }

        if (searchDto.getHasDormitory() != null) {
            spec = spec.and((root, query, builder) ->
                    builder.equal(root.get("dormitory"), searchDto.getHasDormitory()));
        }

        if (searchDto.getRating() > 0) {
            spec = spec.and((root, query, builder) ->
                    builder.ge(root.get("rating"), searchDto.getRating()));
        }

        Pageable pageable = PageRequest.of(searchDto.getPage(), searchDto.getSize(), Sort.by(Sort.Direction.DESC, "rating"));

        return universityRepository.findAll(spec, pageable)
                .map(entity -> {
            var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
            var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
            return adapter.entity2Dto(entity, universityAddress, faculty);
        });
    }

    public List<UniversityDto> getTopUniversities(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "rating"));
        log.info("Getting top {} universities by rating", limit);
        var page = universityRepository.findAllByActiveTrue(pageable);
        return page.getContent().stream().map(entity -> {
            var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
            var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
            return adapter.entity2Dto(entity, universityAddress, faculty);
        }).collect(Collectors.toList());
    }

    public UniversityDto updateLogo(Long id, String logoUrl) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Updating logo for university with id: {}", id);
        entity.setLogoUrl(logoUrl);
        saveUniversity(entity);
        var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
        var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
        return adapter.entity2Dto(entity, universityAddress, faculty);
    }

    public UniversityDto activateUniversity(Long id, boolean active) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Setting active status of university with id: {} to {}", id, active);
        entity.setActive(active);
        saveUniversity(entity);
        var universityAddress = universityAddressService.getUniversityAddressByUniversityId(entity.getId());
        var faculty = facultyService.getFacultiesByUniversityId(entity.getId());
        return adapter.entity2Dto(entity, universityAddress, faculty);
    }

}
