package ru.nhp.core.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.enums.ServiceErrors;
import ru.nhp.core.properties.CartServiceIntegrationProperties;

import java.rmi.ServerException;
import java.rmi.server.ServerNotActiveException;

@Slf4j
@Component
@EnableConfigurationProperties(
        CartServiceIntegrationProperties.class
)
@RequiredArgsConstructor
public class CartServiceIntegration {
    private final static String CLEAR_USER_CART_URI = "/api/v1/carts/0/clear";
    private final static String GET_USER_CART_URI = "/api/v1/carts/0";
    private final WebClient.Builder webClient;
    private final CartServiceIntegrationProperties cartServiceIntegrationProperties;

    public void clearUserCart(String username) {
        getOnStatus(CLEAR_USER_CART_URI, username)
                .toBodilessEntity()
                .block();
    }

    public CartDto getUserCart(String username) {
        return getOnStatus(GET_USER_CART_URI, username)
                .bodyToMono(CartDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String uri, String username) {
        return webClient
                .baseUrl(cartServiceIntegrationProperties.getUrl())
                .build()
                .get()
                .uri(uri)
                .header("username", username)
                .retrieve()
                .onStatus(
                        HttpStatus::is4xxClientError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.NOT_FOUND.name())) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                        return new ResourceNotFoundException("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                    }
                                    log.error("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                    return new ResourceNotFoundException("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                }
                        )
                )
                .onStatus(
                        HttpStatus::is5xxServerError,
                        clientResponse -> clientResponse.bodyToMono(AppError.class).map(
                                body -> {
                                    if (body.getStatusCode().equals(ServiceErrors.SERVICE_UNAVAILABLE.name())) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                        return new ServerNotActiveException("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                    }
                                    log.error("Не выполнено. Внутренняя ошибка сервера.");
                                    return new ServerException("Не выполнено. Внутренняя ошибка сервера.");
                                }
                        )
                );
    }
}
