package kz.abylkhaiyrov.unirateplatformregistry.repository;

import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(Integer code);

    Optional<User> findByEmailAndActiveTrue(String email);

    Optional<User> findByEmailAndIsDeletedFalseAndIsActiveTrue(String login);
}
