package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAdapter;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.EmptyException;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityService {

    private final UniversityRepository universityRepository;
    private final UniversityAdapter adapter;

    public UniversityDto createUniversity(UniversityDto dto){
        log.info("Creating university with UniversityDto:{}", dto);
        var entity = new University();
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setContactEmail(dto.getContactEmail());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setLocation(dto.getLocation());
        entity.setWebsite(dto.getWebsite());
        entity.setLogoUrl(dto.getLogoUrl());
        entity.setContactEmail(dto.getContactEmail());
        entity.setBaseCost(dto.getBaseCost());
        entity.setRating(BigDecimal.ZERO);
        entity.setRatingCount(dto.getRatingCount());
        saveUniversity(entity);
        log.info("University created with University:{}", entity);
        return adapter.entity2Dto(entity);
    }

    private void saveUniversity(University entity){
        universityRepository.save(entity);
    }

    public UniversityDto getUniversityByName(String name){
        if (name == null){
            throw new EmptyException("University name cannot be null");
        }

        var entity = universityRepository.findByName(name)
                .orElseThrow(() -> new NotFoundException("University not found with name: " + name));
        return adapter.entity2Dto(entity);
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
        var entity = universityRepository.findById(universityId)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + universityId));

        var oldAvg = entity.getRating();
        var oldCount = entity.getRatingCount();

        var newAvg = oldAvg
                .multiply(BigDecimal.valueOf(oldCount))
                .add(BigDecimal.valueOf(newRating))
                .divide(BigDecimal.valueOf(oldCount + 1), 2, RoundingMode.HALF_UP);

        entity.setRating(newAvg);
        entity.setRatingCount(oldCount + 1);
        saveUniversity(entity);
    }

    public List<UniversityDto> getAllUniversities(){
        var list = universityRepository.findAll();
        return list.stream().map(adapter::entity2Dto).collect(Collectors.toList());
    }

    public Page<UniversityDto> getUniversitiesByPage(Pageable pageable) {
        return universityRepository.findAllByActiveTrue(pageable).map(adapter::entity2Dto);
    }

    public UniversityDto updateUniversity(Long id, UniversityDto dto) {
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
        return adapter.entity2Dto(entity);
    }

    public void deleteUniversity(Long id) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Deleting (deactivating) university with id: {}", id);
        entity.setActive(false);
        saveUniversity(entity);
    }

    public List<UniversityDto> searchUniversities(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()){
            throw new EmptyException("Search keyword cannot be empty");
        }
        log.info("Searching universities with keyword: {}", keyword);
        var list = universityRepository.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
        return list.stream().map(adapter::entity2Dto).collect(Collectors.toList());
    }

    public List<UniversityDto> getTopUniversities(int limit) {
        Pageable pageable = PageRequest.of(0, limit, Sort.by(Sort.Direction.DESC, "rating"));
        log.info("Getting top {} universities by rating", limit);
        var page = universityRepository.findAllByActiveTrue(pageable);
        return page.getContent().stream().map(adapter::entity2Dto).collect(Collectors.toList());
    }

    public UniversityDto updateLogo(Long id, String logoUrl) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Updating logo for university with id: {}", id);
        entity.setLogoUrl(logoUrl);
        saveUniversity(entity);
        return adapter.entity2Dto(entity);
    }

    public UniversityDto activateUniversity(Long id, boolean active) {
        var entity = universityRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University not found with id: " + id));
        log.info("Setting active status of university with id: {} to {}", id, active);
        entity.setActive(active);
        saveUniversity(entity);
        return adapter.entity2Dto(entity);
    }

}
