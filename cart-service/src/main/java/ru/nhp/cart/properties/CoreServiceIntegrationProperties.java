package ru.nhp.cart.properties;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@Data
@NoArgsConstructor
@ConfigurationProperties(prefix = "integrations.core-service")
public class CoreServiceIntegrationProperties {
    private Integer connectTimeout;
    private Integer readTimeout;
    private Integer writeTimeout;
    private String url;
}
