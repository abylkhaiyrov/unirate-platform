package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Forum;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumRepository extends JpaRepository<Forum, Long> {

    List<Forum> findByNameContainingIgnoreCase(String name);

    List<Forum> findAllByUniversity(University university);

}
