package kz.abylkhaiyrov.unirateplatformregistry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
//@EnableEurekaClient
public class UniratePlatformRegistryApplication {

    public static void main(String[] args) {
        SpringApplication.run(UniratePlatformRegistryApplication.class, args);
    }

}
