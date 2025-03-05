package kz.abylkhaiyrov.unirateplatformuniversity.configuration;

import feign.FeignException;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.SneakyThrows;
import org.apache.http.HttpStatus;

public class CustomErrorDecoder implements ErrorDecoder  {
    @SneakyThrows
    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.SC_FORBIDDEN ||
                response.status() == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return new FeignException.Forbidden(
                    "Access is forbidden", response.request(),
                    null, response.headers());
        }
        return null;
    }
}
