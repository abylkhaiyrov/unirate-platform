package kz.abylkhaiyrov.unirateplatformuniversity.repository;

import kz.abylkhaiyrov.unirateplatformuniversity.entity.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    List<Favorite> findAllByUserId(Long userId);

}
