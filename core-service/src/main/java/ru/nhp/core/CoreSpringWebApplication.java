package ru.nhp.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class CoreSpringWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(CoreSpringWebApplication.class, args);
	}
}
