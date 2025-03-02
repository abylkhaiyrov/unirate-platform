package kz.abylkhaiyrov.unirateplatformregistry.repository;

import kz.abylkhaiyrov.unirateplatformregistry.entity.Role;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole, Long> {

    Optional<UserRole> findByUserAndDeletedFalseAndEndDateIsNull(User user);

    Optional<UserRole> findByUserAndRoleAndDeletedFalseAndEndDateIsNull(User user, Role role);

}
