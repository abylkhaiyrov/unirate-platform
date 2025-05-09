package kz.abylkhaiyrov.unirateplatformregistry.service;

import kz.abylkhaiyrov.unirateplatformregistry.adapter.UserAdapter;
import kz.abylkhaiyrov.unirateplatformregistry.dto.ChangePasswordDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.enums.Role;
import kz.abylkhaiyrov.unirateplatformregistry.repository.UserRepository;
import kz.abylkhaiyrov.unirateplatformregistry.util.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final UserAdapter adapter;
    private final UserRoleService userRoleService;
    private final PasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Optional<User> findActiveByEmail(String login) {
        return userRepository.findByEmailAndIsActiveTrue(login);
    }

    @Transactional
    public void save(User user) {
        userRepository.save(user);
    }

    public UserDto getCurrentUser() {
        var currentPerson = SecurityUtils.getCurrentUser();
        UserDto userDto = adapter.toDto(currentPerson);
        userRoleService.findActiveByUser(currentPerson).ifPresent(userRole -> {
            userDto.setRole(Role.valueOf(userRole.getCode()));
        });
        return userDto;
    }

    public UserDto findById(Long id) {
        var byId = userRepository.findById(id);
        if (byId.isEmpty()) {
            throw new IllegalArgumentException("User doesnt exists");
        }
        var userDto = adapter.toDto(byId.get());
        userRoleService.findActiveByUser(byId.get()).ifPresent(userRole -> {
            userDto.setRole(Role.valueOf(userRole.getCode()));
        });

        return userDto;
    }

    public String updateUserProfile(Long userId, String url) {
        var user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User doesn't exist with id: " + userId));
        try {
            if (url != null && (user.getProfileImageUrl() == null || !user.getProfileImageUrl().equals(url))) {
                user.setProfileImageUrl(url);
            }
            userRepository.save(user);
            return "Profile updated successfully";
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException("Profile update failed");
        }
    }

    @Transactional
    public UserDto updateById(Long id, UserDto userDto) {
        var byId = userRepository.findById(id);
        if (byId.isEmpty())  {
            throw new IllegalArgumentException("User doesnt exists");
        }
        var user = byId.get();
        if (Objects.nonNull(userDto.getFirstName()) && (user.getFirstName() == null ||
                !user.getFirstName().equals(userDto.getFirstName()))) {
            user.setFirstName(userDto.getFirstName());
        }
        if (Objects.nonNull(userDto.getLastName()) && (user.getLastName() == null ||
                !user.getLastName().equals(userDto.getLastName()))) {
            user.setLastName(userDto.getLastName());
        }
        if (Objects.nonNull(userDto.getTelephone()) && (user.getTelephone() == null ||
                !user.getTelephone().equals(userDto.getTelephone()))) {
            user.setTelephone(userDto.getTelephone());
        }
        if (Objects.nonNull(userDto.getEmail()) && (user.getEmail() == null ||
                !user.getEmail().equals(userDto.getEmail()))) {
            user.setEmail(userDto.getEmail());
        }
        if (Objects.nonNull(userDto.getStatus()) && (user.getStatus() == null ||
                !user.getStatus().equals(userDto.getStatus()))) {
            user.setStatus(userDto.getStatus());
        }
        var save = userRepository.save(user);
        var userDto1 = adapter.toDto(user);
        userRoleService.findActiveByUser(save).ifPresent(userRole ->
                userDto1.setRole(Role.valueOf(userRole.getCode())));
        return adapter.toDto(user);
    }

    @Transactional
    public Optional<User> findByActivationCode(Integer activationCode) {
        return userRepository.findByActivationCode(activationCode);
    }

    @Transactional
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional(readOnly = true)
    public Page<UserDto> findAll(Pageable pageable) {
        return userRepository.findAll(pageable).map(adapter::toDto);
    }

    @Transactional
    public boolean changePassword(ChangePasswordDto dto) {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        var userOptional = userRepository.findByEmail(email);
        if (userOptional.isEmpty()) {
            return false;
        }
        var user = userOptional.get();
        if (!passwordEncoder.matches(dto.getOldPassword(), user.getPassword())) {
            return false;
        }
        user.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        userRepository.save(user);
        return true;
    }

}