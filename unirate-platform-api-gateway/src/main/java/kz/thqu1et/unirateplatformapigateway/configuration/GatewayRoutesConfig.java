package kz.thqu1et.unirateplatformapigateway.configuration;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayRoutesConfig {

    @Bean
    public RouteLocator customRoutes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("unirate-platform-registry", r-> r
                        .path("/users/**")
                        .filters(f -> f.stripPrefix(1))
                        .uri("http://localhost:8087")
                )
                .route("unirate-platform-university", r -> r
                        .path("/university/**")
                        .uri("http://localhost:8082")
                )
                .build();
    }
}
