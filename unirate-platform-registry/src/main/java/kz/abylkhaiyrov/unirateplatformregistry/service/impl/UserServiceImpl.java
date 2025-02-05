package kz.abylkhaiyrov.unirateplatformregistry.service.impl;

import kz.abylkhaiyrov.unirateplatformregistry.adapter.UserAdapter;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserUpdateDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.Role;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import kz.abylkhaiyrov.unirateplatformregistry.exception.UserNotFoundException;
import kz.abylkhaiyrov.unirateplatformregistry.repository.UserRepository;
import kz.abylkhaiyrov.unirateplatformregistry.security.JWTAuthenticationFilter;
import kz.abylkhaiyrov.unirateplatformregistry.service.UserService;
import kz.abylkhaiyrov.unirateplatformregistry.util.JwtTokenUtil;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.jose4j.jwt.JwtClaims;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserAdapter adapter;

    @Override
    public JwtClaims getDataByToken(String token) throws Exception {
        String jwt = JWTAuthenticationFilter.parsJwt(token);
        return JwtTokenUtil.getDataJWT(jwt);
    }

    @Override
    public User createUser(UserRegisterDto dto) {

        var entity = adapter.toEntity(dto);
        entity.setRole(Role.USER);
        entity.setActive(true);
        entity = userRepository.save(entity);

        return entity;
    }

    @SneakyThrows
    @Override
    public UserDto updateUser(String token, Long userId, UserUpdateDto dto) {
        var user = getDataByToken(token);
        var entity = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with id: " + userId));

        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());

        entity = userRepository.save(entity);
        return adapter.toDto(entity);
    }

    @Override
    public UserDto getUserById(Long id) {
        var user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found with id: %s", id)
                ));
        return adapter.toDto(user);
    }

    @Override
    public UserDto getUserByEmail(String token, String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found with email: %s", email)
                ));
        return adapter.toDto(user);
    }

    @Override
    public UserDto getUserByUsername(String token, String username) {
        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User not found with username: %s", username)
                ));
        return adapter.toDto(user);
    }

    @SneakyThrows
    @Override
    public void deleteUserById(String token, Long id) {
        var user = getDataByToken(token);
        userRepository.deleteById(id);
    }

    @Override
    @SneakyThrows
    public void deleteUserByEmail(String token, String email) {
        var user = getDataByToken(token);
        userRepository.deleteByEmail(email);
    }

    @Override
    @SneakyThrows
    public void deleteUserByUsername(String token, String username) {
        var user = getDataByToken(token);
        userRepository.deleteByUsername(username);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

}
