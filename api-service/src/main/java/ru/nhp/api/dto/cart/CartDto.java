package ru.nhp.api.dto.cart;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель корзины")
public class CartDto {
    @Schema(description = "Элементы корзины", required = true, example = "{1, Товар№1, 4, 500.00, 1500.00}")
    private List<CartItemDto> items;
    @Schema(description = "Стоимость корзины", required = true, example = "1500.00")
    private BigDecimal totalPrice;
}
