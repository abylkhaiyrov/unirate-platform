package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByUniversity(University university, Pageable pageable);

    List<Review> findByUniversity(University university);

}
