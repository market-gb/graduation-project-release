package ru.nhp.core.integrations;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.core.exceptions.CartServiceIntegrationException;
import ru.nhp.core.exceptions.CoreAppError;

@Slf4j
@Component
@RequiredArgsConstructor
public class CartServiceIntegration {
    private final static String clearUserCartUri = "/api/v1/cart/0/clear";
    private final static String getUserCartUri = "/api/v1/cart/0";
    private final WebClient cartServiceWebClient;

    public void clearUserCart(String username) {
        getOnStatus(clearUserCartUri, username)
                .toBodilessEntity()
                .block();
    }

    public CartDto getUserCart(String username) {
        return getOnStatus(getUserCartUri, username)
                .bodyToMono(CartDto.class)
                .block();
    }

    private WebClient.ResponseSpec getOnStatus(String uri, String username) {
        return cartServiceWebClient.get()
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
