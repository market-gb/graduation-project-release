package ru.nhp.analytics;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class AnalyticsSpringWebApplication {
	public static void main(String[] args) {
		SpringApplication.run(AnalyticsSpringWebApplication.class, args);
	}
}
