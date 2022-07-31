package ru.nhp.cart.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nhp.api.dto.core.ProductDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.enums.ServiceErrors;
import ru.nhp.cart.properties.CoreServiceIntegrationProperties;

import java.rmi.ServerException;
import java.rmi.server.ServerNotActiveException;
import java.util.Optional;

@Component
@EnableConfigurationProperties({
        CoreServiceIntegrationProperties.class,
})
@RequiredArgsConstructor
@Slf4j
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
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.NOT_FOUND.name())) {
                                        log.error("Выполнен некорректный запрос к сервису Core: Товар не найден");
                                        return new ResourceNotFoundException("Выполнен некорректный запрос к сервису Core: Товар не найден");
                                    }
                                    log.error("Выполнен некорректный запрос к сервису Core: причина неизвестна");
                                    return new ResourceNotFoundException("Выполнен некорректный запрос к сервису Core: причина неизвестна");
                                }
                        )
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.SERVICE_UNAVAILABLE.name())) {
                                        log.error("Выполнен некорректный запрос к сервису Core: Core сломан");
                                        return new ServerNotActiveException("Выполнен некорректный запрос к сервису Core: Core сломан");
                                    }
                                    log.error("Не выполнено. Внутренняя ошибка сервера.");
                                    return new ServerException("Не выполнено. Внутренняя ошибка сервера.");
                                }
                        )
                )
                .bodyToMono(ProductDto.class)
                .block();
        return Optional.ofNullable(productDto);
    }
}
