package ru.nhp.core.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.core.exceptions.CartServiceIntegrationException;
import ru.nhp.core.exceptions.CoreAppError;
import ru.nhp.core.properties.CartServiceIntegrationProperties;

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
                        clientResponse -> clientResponse.bodyToMono(CoreAppError.class).map(
                                body -> {
                                    if (body.getCode().equals(CoreAppError.ServiceErrors.CART_NOT_FOUND)) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина не найдена");
                                    }
                                    if (body.getCode().equals(CoreAppError.ServiceErrors.CART_SERVICE_IS_BROKEN)) {
                                        log.error("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                        return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: корзина сломана");
                                    }
                                    log.error("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                    return new CartServiceIntegrationException("Выполнен некорректный запрос к сервису корзин: причина неизвестна");
                                }
                        )
                );
    }
}
