package kz.abylkhaiyrov.unirateplatformeurekaserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class UniratePlatformEurekaServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniratePlatformEurekaServerApplication.class, args);
    }

}
