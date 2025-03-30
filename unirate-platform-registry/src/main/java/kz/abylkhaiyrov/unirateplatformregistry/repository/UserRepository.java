package kz.abylkhaiyrov.unirateplatformregistry.repository;

import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    Optional<User> findByActivationCode(Integer code);

    @Query(value = "select * from _user where email = :email and active = true", nativeQuery = true)
    Optional<User> findByEmailAndIsActiveTrue(@Param("email") String email);

    Optional<User> findByEmailAndIsDeletedFalseAndIsActiveTrue(String login);
}
