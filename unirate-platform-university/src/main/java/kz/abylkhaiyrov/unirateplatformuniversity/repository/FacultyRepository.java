package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Faculty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FacultyRepository extends JpaRepository<Faculty, Long> {

    Page<Faculty> findAllByActiveTrue(Pageable pageable);

    Optional<Faculty> findByNameContaining(String name);

    List<Faculty> findByUniversityId(Long universityId);

}
