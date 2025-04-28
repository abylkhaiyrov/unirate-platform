package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Forum;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.University;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findByForum(Forum forum, Pageable pageable);

    List<Review> findByForum(Forum forum);

    @Query("SELECT r FROM Review r WHERE r.forum IN :forums ORDER BY r.createdDate DESC")
    List<Review> findAllByForumsAndCreatedDateDesc(@Param("forums") List<Forum> forums, Pageable pageable);

}
