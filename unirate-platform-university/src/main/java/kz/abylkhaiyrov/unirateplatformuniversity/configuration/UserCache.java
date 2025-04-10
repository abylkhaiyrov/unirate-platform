package kz.abylkhaiyrov.unirateplatformuniversity.configuration;

import kz.abylkhaiyrov.unirateplatformuniversity.client.UserClient;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UserDto;
import lombok.extern.slf4j.Slf4j;
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
            var response = userClient.findUserById(id);
            UserDto user = (response != null) ? response.getBody() : null;
            if (user == null) {
                log.warn("User must be null");
                user = new UserDto();
            }
            return user;
        });
    }
}
