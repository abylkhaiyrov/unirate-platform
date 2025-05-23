package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findAllByActiveTrue(Pageable pageable);
}
