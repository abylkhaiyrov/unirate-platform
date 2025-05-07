package kz.abylkhaiyrov.unirateplatformuniversity.service;

import kz.abylkhaiyrov.unirateplatformuniversity.adapter.UniversityAddressMapper;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UniversityAddressDto;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import kz.abylkhaiyrov.unirateplatformuniversity.exception.NotFoundException;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityAddressRepository;
import kz.abylkhaiyrov.unirateplatformuniversity.repository.UniversityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UniversityAddressService {

    private final UniversityAddressRepository universityAddressRepository;
    private final UniversityAddressMapper universityAddressMapper;
    private final UniversityRepository universityRepository;

    // Создание нового адреса университета
    public UniversityAddressDto createAddress(UniversityAddressDto dto) {
        log.info("Создание UniversityAddress {}", dto);
        var entity = new UniversityAddress();
        entity.setCity(dto.getCity());
        entity.setFullAddress(dto.getFullAddress());
        entity.setRegion(dto.getRegion());
        var university = universityRepository.findById(dto.getUniversityId())
                .orElseThrow(() -> new NotFoundException("University not found with id: " + dto.getUniversityId()));
        entity.setUniversity(university);
        entity = saveAddress(entity);
        return universityAddressMapper.entity2Dto(entity);
    }

    private UniversityAddress saveAddress(UniversityAddress entity) {
        return universityAddressRepository.save(entity);
    }

    public UniversityAddressDto getAddressById(Long id) {
        log.info("Получение UniversityAddress по id: {}", id);
        var universityAddress = universityAddressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University Address not found with id: " + id));
        return universityAddressMapper.entity2Dto(universityAddress);
    }

    public Page<UniversityAddressDto> getAddresses(Pageable pageable, String city) {
        log.info("Получение UniversityAddresses для города: {}", city);
        return universityAddressRepository.findAllByCity(city, pageable)
                .map(universityAddressMapper::entity2Dto);
    }

    public UniversityAddressDto updateAddress(Long id, UniversityAddressDto dto) {
        log.info("Обновление UniversityAddress с id: {}", id);
        var existing = universityAddressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University Address not found with id: " + id));

        existing.setCity(dto.getCity());
        existing.setFullAddress(dto.getFullAddress());
        existing.setRegion(dto.getRegion());
        if (dto.getUniversityId() != null) {
            var university = universityRepository.findById(dto.getUniversityId())
                    .orElseThrow(() -> new NotFoundException("University not found with id: " + dto.getUniversityId()));
            existing.setUniversity(university);
        }

        existing = saveAddress(existing);
        return universityAddressMapper.entity2Dto(existing);
    }

    public void deleteAddress(Long id) {
        log.info("Удаление UniversityAddress с id: {}", id);
        var existing = universityAddressRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("University Address not found with id: " + id));
        universityAddressRepository.delete(existing);
    }

    public Optional<UniversityAddress> getUniversityAddressByUniversityId(Long universityId) {
        return universityAddressRepository.findByUniversityId(universityId);
    }
}