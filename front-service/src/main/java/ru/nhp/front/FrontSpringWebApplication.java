package ru.nhp.front;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;


@SpringBootApplication
@EnableEurekaClient
public class FrontSpringWebApplication {
    public static void main(String[] args){
        SpringApplication.run(FrontSpringWebApplication.class, args);
    }
}
