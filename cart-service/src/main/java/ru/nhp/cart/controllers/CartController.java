package ru.nhp.cart.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.nhp.api.dto.StringResponse;
import ru.nhp.api.dto.cart.CartDto;
import ru.nhp.cart.converters.CartConverter;
import ru.nhp.cart.services.CartService;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
@Tag(name = "Корзина", description = "Методы работы с корзиной")
public class CartController {
    private final CartService cartService;
    private final CartConverter cartConverter;

    @Operation(
            summary = "Запрос на получение корзины по идентификационному номеру пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = CartDto.class))
                    )
            }
    )
    @GetMapping("/{uuid}")
    public CartDto getCurrentCart(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                           @PathVariable @Parameter(description = "Идентификатор пользователя", required = true)String uuid) {
        return cartConverter.modelToDto(cartService.getCurrentCart(getCurrentCartUuid(username, uuid)));
    }

    @Operation(
            summary = "Генерация UUID корзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200",
                            content = @Content(schema = @Schema(implementation = StringResponse.class))
                    )
            }
    )
    @GetMapping("/generate")
    public StringResponse getCartUuid() {
        return new StringResponse(cartService.generateCartUuid());
    }

    @Operation(
            summary = "Добавление продукта в корзину",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/add/{productId}")
    public void add(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                    @PathVariable @Parameter(description = "Идентификатор корзины", required = true) String uuid,
                    @PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long productId) {
        cartService.addToCart(getCurrentCartUuid(username, uuid), productId);
    }

    @Operation(
            summary = "Уменьшение количества продукта в корзине",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/decrement/{productId}")
    public void decrement(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                          @PathVariable @Parameter(description = "Идентификатор корзины", required = true) String uuid,
                          @PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long productId) {
        cartService.decrementItem(getCurrentCartUuid(username, uuid), productId);
    }

    @Operation(
            summary = "Удаление единицы продукта из корзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/remove/{productId}")
    public void remove(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                       @PathVariable @Parameter(description = "Идентификатор корзины", required = true) String uuid,
                       @PathVariable @Parameter(description = "Идентификатор продукта", required = true) Long productId) {
        cartService.removeItemFromCart(getCurrentCartUuid(username, uuid), productId);
    }

    @Operation(
            summary = "Очистка корзины",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/clear")
    public void clear(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                      @PathVariable @Parameter(description = "Идентификатор корзины", required = true) String uuid) {
        cartService.clearCart(getCurrentCartUuid(username, uuid));
    }

    @Operation(
            summary = "Слияние корзин не авторизованного и авторизованного пользователя",
            responses = {
                    @ApiResponse(
                            description = "Успешный ответ", responseCode = "200"
                    )
            }
    )
    @GetMapping("/{uuid}/merge")
    public void merge(@RequestHeader(required = false) @Parameter(description = "Имя пользователя") String username,
                      @PathVariable @Parameter(description = "Идентификатор корзины", required = true) String uuid) {
        cartService.merge(
                getCurrentCartUuid(username, null),
                getCurrentCartUuid(null, uuid)
        );
    }


    private String getCurrentCartUuid(String username, String uuid) {
        if (username != null) {
            return cartService.getCartUuidFromSuffix(username);
        }
        return cartService.getCartUuidFromSuffix(uuid);
    }
}
