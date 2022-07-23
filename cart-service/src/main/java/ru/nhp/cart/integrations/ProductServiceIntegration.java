package ru.nhp.cart.integrations;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.cart.properties.CoreServiceIntegrationProperties;

import java.util.Optional;

@Component
@EnableConfigurationProperties({
        CoreServiceIntegrationProperties.class,
})
@RequiredArgsConstructor
public class ProductServiceIntegration {
    private final WebClient.Builder webClient;
    private final CoreServiceIntegrationProperties coreServiceIntegrationProperties;

    public Optional<ProductDto> findById(Long id) {
        ProductDto productDto = webClient
                .baseUrl(coreServiceIntegrationProperties.getUrl())
                .build()
                .get()
                .uri("/api/v1/products/" + id)
                .retrieve()
                .bodyToMono(ProductDto.class)
                .block();
        return Optional.ofNullable(productDto);
    }
}
