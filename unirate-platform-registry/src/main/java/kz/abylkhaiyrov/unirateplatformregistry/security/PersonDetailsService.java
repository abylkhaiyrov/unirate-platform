package kz.abylkhaiyrov.unirateplatformregistry.security;

import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.entity.UserRole;
import kz.abylkhaiyrov.unirateplatformregistry.repository.UserRepository;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

@Configuration
public class PersonDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final UserRoleService userRoleService;

    @Autowired
    public PersonDetailsService(UserRepository userRepository, UserRoleService userRoleService) {
        this.userRepository = userRepository;
        this.userRoleService = userRoleService;
    }

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User byLoginAndIsDeletedFalseAndIsActiveTrue =
                userRepository.findByEmailAndIsDeletedFalseAndIsActiveTrue(username)
                        .orElseThrow(
                                () -> new UsernameNotFoundException(String.format("User with username %s not found",
                                        username)));

        UserRole userRole = userRoleService.findActiveByUser(byLoginAndIsDeletedFalseAndIsActiveTrue).orElseThrow(
                () -> new UsernameNotFoundException(String.format("User with username %s not found",
                        username)));

        return new PersonDetails(byLoginAndIsDeletedFalseAndIsActiveTrue, userRole);
    }

}
