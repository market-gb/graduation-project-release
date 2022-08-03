package ru.nhp.api.dto.analitics;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель товара со статистикой")
public class ProductAnaliticsDto {

    @NotBlank(message = "Поле названия товара не должно быть пустым")
    @Size(min = 5, message = "Название товара должно быть не короче 5 символов")
    @Schema(description = "Название товара", required = true, maxLength = 255, minLength = 3, example = "Товар#1")
    private String title;

    @NotNull(message = "Поле количества товара не должно быть пустым")
    @DecimalMin(value = "1", inclusive = false)
    @Digits(integer = 10, fraction = 0)
    @Schema(description = "Количество товара", required = true, example = "111")
    private Long quantity;
}
