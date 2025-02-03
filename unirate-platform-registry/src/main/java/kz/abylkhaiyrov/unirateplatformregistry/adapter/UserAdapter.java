package kz.abylkhaiyrov.unirateplatformregistry.adapter;

import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static kz.abylkhaiyrov.unirateplatformregistry.util.ICommons.mapUserRole;

@Component
public class UserAdapter {

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserAdapter(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    /*
     Преобразование DTO Registration в Entity
     */
    public User toEntity(UserRegisterDto userDto) {
        if (userDto == null) {
            return null;
        }
        var userEntity = new User();
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(passwordEncoder.encode(userDto.getPassword()));
        userEntity.setUsername(userDto.getUsername());
        return userEntity;
    }

    /*
    Преобразование DTO в Entity
     */
    public User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        var userEntity = new User();
        userEntity.setId(userDto.getId());
        userEntity.setEmail(userDto.getEmail());
        userEntity.setPassword(userDto.getPassword());
        userEntity.setUsername(userDto.getUsername());
        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());
        userEntity.setActive(userDto.getActive());
        userEntity.setRole(mapUserRole(userDto.getRole()));
        return userEntity;
    }

    /*
     Преобразование Entity в DTO
     */
    public UserDto toDto(User userEntity) {
        if (userEntity == null) {
            return null;
        }
        var dto = new UserDto();
        dto.setId(userEntity.getId());
        dto.setUsername(userEntity.getUsername());
        dto.setEmail(userEntity.getEmail());
        dto.setFirstName(userEntity.getFirstName());
        dto.setLastName(userEntity.getLastName());
        dto.setActive(userEntity.getActive());
        dto.setRole(String.valueOf(userEntity.getRole()));
        dto.setPassword(userEntity.getPassword());
        return dto;
    }

}
