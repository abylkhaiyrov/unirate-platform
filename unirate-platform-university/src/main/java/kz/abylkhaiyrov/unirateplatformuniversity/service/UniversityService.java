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
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
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

        universityRepository.save(entity);
    }

    public List<UniversityDto> getAllUniversities(){
        var list = universityRepository.findAll();

        return list.stream().map(adapter::entity2Dto).collect(Collectors.toList());
    }

    public Page<UniversityDto> getUniversitiesByPage(Pageable pageable){
        var list = universityRepository.findAll(pageable);

        return null;
    }

}
