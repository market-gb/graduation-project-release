package ru.nhp.api.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;
import ru.nhp.api.enums.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель заказа")
public class OrderDto {
    @Schema(description = "Идентификатор заказа", required = true, example = "1")
    private Long id;
    @Schema(description = "Имя получателя", required = true, example = "Иванов Иван Иванович")
    private String username;
    @Schema(description = "Список покупок", required = true, example = "{Товар#1 - 4 шт., Товар#2 - 3 шт.}")
    private Set<OrderItemDto> items;
    @Schema(description = "Стоимость заказа", required = true, example = "1000.00")
    private BigDecimal totalPrice;
    @Schema(description = "Адрес доставки", required = true, example = "603000, г. Москва, ул. Прямая, д.10, кв.1")
    private String address;
    @Schema(description = "Телефон получателя", required = true, example = "222-22-22")
    private String phone;
    @Schema(description = "Статус заказа", required = true, example = "CREATED")
    private OrderStatus orderStatus;
}
