package ru.nhp.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class GatewaySpringWebApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewaySpringWebApplication.class, args);
    }
}
