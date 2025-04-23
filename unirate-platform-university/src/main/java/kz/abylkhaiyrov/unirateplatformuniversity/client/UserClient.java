package kz.abylkhaiyrov.unirateplatformuniversity.client;

import kz.abylkhaiyrov.unirateplatformuniversity.configuration.FeignClientInterceptor;
import kz.abylkhaiyrov.unirateplatformuniversity.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(
        name = "UNIRATE-PLATFORM-REGISTRY",
        url = "https://unirate.kz",
        configuration = FeignClientInterceptor.class,
        fallback = UserClientFallback.class  ,
        decode404 = true,
        path = "/registry"
)
public interface UserClient {

    @GetMapping(value = "/open-api/auth/{id}")
    ResponseEntity<UserDto> findUserById(@PathVariable("id") Long id);

}