package kz.abylkhaiyrov.unirateplatformregistry.service;

import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.entity.UserRole;
import kz.abylkhaiyrov.unirateplatformregistry.enums.Role;
import kz.abylkhaiyrov.unirateplatformregistry.repository.RoleRepository;
import kz.abylkhaiyrov.unirateplatformregistry.repository.UserRoleRepository;
import kz.abylkhaiyrov.unirateplatformregistry.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserRoleService {

    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Transactional
    public UserRole createUserRole(User user, Role role) {
        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setBeginDate(LocalDateTime.now());
        userRole.setCode(role.name());
        Optional<kz.abylkhaiyrov.unirateplatformregistry.entity.Role> byCodeAndDeletedFalse = roleRepository.findByCodeAndDeletedFalse(role.name());
        byCodeAndDeletedFalse.ifPresent(userRole::setRole);

        return userRoleRepository.save(userRole);
    }

    public Optional<UserRole> findActiveByUser(User user) {
        return userRoleRepository.findByUserAndDeletedFalseAndEndDateIsNull(user);
    }

    public boolean hasRole(String role) {
        return findActiveRoleByAuthUser(role).isPresent();
    }

    public Optional<UserRole> findActiveRoleByAuthUser(String role) {
        var byCodeAndDeletedFalse =
                roleRepository.findByCodeAndDeletedFalse(role);
        if (byCodeAndDeletedFalse.isEmpty()) {
            throw new IllegalArgumentException("Role is not found");
        }
        var currentUser = SecurityUtils.getCurrentUser();
        if (currentUser == null) {
            throw new IllegalArgumentException("User not authorized");
        }
        return userRoleRepository.findByUserAndRoleAndDeletedFalseAndEndDateIsNull(currentUser,
                byCodeAndDeletedFalse.get());
    }

}
