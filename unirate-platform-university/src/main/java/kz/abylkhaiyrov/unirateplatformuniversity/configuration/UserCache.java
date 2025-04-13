package kz.abylkhaiyrov.unirateplatformuniversity.configuration;

import kz.abylkhaiyrov.unirateplatformuniversity.client.UserClient;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@Component
public class UserCache {

    private final UserClient userClient;
    private final ConcurrentHashMap<Long, UserDto> cache = new ConcurrentHashMap<>();

    public UserCache(UserClient userClient) {
        this.userClient = userClient;
    }

    public UserDto getUserById(Long userId) {
        return cache.computeIfAbsent(userId, id -> {
            try {
                ResponseEntity<UserDto> response = userClient.findUserById(id);
                if (response != null && response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                    return response.getBody();
                }
                log.warn("Пользователь с id {} не найден. Используем fallback-значение.", id);
            } catch (Exception ex) {
                log.error("Ошибка при получении пользователя с id {}: {}", id, ex.getMessage());
            }
            UserDto defaultUser = new UserDto();
            defaultUser.setId(id);
            defaultUser.setUsername("Unknown");
            return defaultUser;
        });
    }
}