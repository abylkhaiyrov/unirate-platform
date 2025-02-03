package kz.abylkhaiyrov.unirateplatformregistry.repository;

import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByUsername(String username);

    Optional<User> deleteByEmail(String email);

    Optional<User> deleteByUsername(String username);

    Boolean existsByEmail(String email);

    Boolean existsByUsername(String username);
}
