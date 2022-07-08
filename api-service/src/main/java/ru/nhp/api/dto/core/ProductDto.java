package ru.nhp.api.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;
import java.math.BigDecimal;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Модель товара")
public class ProductDto {
    @Schema(description = "Идентификатор товара", required = true, example = "1")
    private Long id;
    @NotBlank(message = "Поле названия товара не должно быть пустым")
    @Size(min = 5, message = "Название товара должно быть не короче 5 символов")
    @Schema(description = "Название товара", required = true, maxLength = 255, minLength = 3, example = "Товар#1")
    private String title;
    @NotNull(message = "Поле цены товара не должно быть пустым")
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 10, fraction = 2)
    @Schema(description = "Цена товара", required = true, example = "500.00")
    private BigDecimal price;
    @NotNull(message = "Должна быть указана хотя бы одна категория")
    @Schema(description = "Категории товара", required = true, example = "{Категория#1, Категория#2}")
    private Set<CategoryDto> categories;
}
