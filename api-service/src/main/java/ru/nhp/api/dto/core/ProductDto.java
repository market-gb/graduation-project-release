package ru.nhp.api.dto.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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

    @Schema(description = "Id картинки товара", required = true, example = "1")
    private Long imageId;

    @NotBlank(message = "Описание товара не должно быть пустым")
    @Size(min = 5, message = "Описание товара должно быть не короче 5 символов")
    @Schema(description = "Описание товара", required = true, maxLength = 255, minLength = 3, example = "Замечательный товар")
    private String description;

    @NotNull(message = "Должна быть указана хотя бы одна категория")
    @Schema(description = "Идентификаторы категорий товара", required = true, example = "{1, 2}")
    @JsonProperty("group_id")
    private Set<Long> groupId;

    @Schema(description = "Время создания товара", required = true, example = "2022.10.10")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @Schema(description = "Время изменения товара", required = true, example = "2022.10.10")
    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}
