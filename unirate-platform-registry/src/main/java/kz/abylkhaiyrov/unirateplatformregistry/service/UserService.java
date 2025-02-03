package kz.abylkhaiyrov.unirateplatformregistry.service;

import kz.abylkhaiyrov.unirateplatformregistry.dto.UserDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.UserUpdateDto;
import kz.abylkhaiyrov.unirateplatformregistry.dto.auth.UserRegisterDto;
import kz.abylkhaiyrov.unirateplatformregistry.entity.User;
import org.jose4j.jwt.JwtClaims;

import java.util.List;

public interface UserService {

    JwtClaims getDataByToken(String token) throws Exception;

    User createUser(UserRegisterDto user);

    UserDto updateUser(String token, Long userId, UserUpdateDto user);

    UserDto getUserById(String token,Long id);

    UserDto getUserByEmail(String token,String email);

    UserDto getUserByUsername(String token, String username);

    void deleteUserById(String token,Long id);

    void deleteUserByEmail(String token,String email);

    void deleteUserByUsername(String token,String username);

    List<User> getAllUsers();

}
