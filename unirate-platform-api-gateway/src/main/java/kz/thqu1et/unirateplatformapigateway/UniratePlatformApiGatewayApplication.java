package kz.thqu1et.unirateplatformapigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class UniratePlatformApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniratePlatformApiGatewayApplication.class, args);
    }

}