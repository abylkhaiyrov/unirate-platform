package kz.abylkhaiyrov.unirateplatformuniversity.client;

import kz.abylkhaiyrov.unirateplatformuniversity.dto.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class UserClientFallback implements UserClient {

    @Override
    public ResponseEntity<UserDto> findUserById(Long id) {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).body(new UserDto());
    }
}
