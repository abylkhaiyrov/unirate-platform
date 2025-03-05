package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Review;
import kz.abylkhaiyrov.unirateplatformuniversity.entity.ReviewComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewCommentRepository extends JpaRepository<ReviewComment, Long> {

    List<ReviewComment> findByReview(Review review);

}
