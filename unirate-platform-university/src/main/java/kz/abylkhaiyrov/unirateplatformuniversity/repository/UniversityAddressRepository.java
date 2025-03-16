package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.UniversityAddress;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UniversityAddressRepository extends JpaRepository<UniversityAddress, Long> {

    @Query("SELECT ua FROM UniversityAddress ua WHERE ua.city = :city")
    Page<UniversityAddress> findAllByCity(@Param("city") String city, Pageable pageable);

    Optional<UniversityAddress> findByUniversityId(Long universityId);

}
