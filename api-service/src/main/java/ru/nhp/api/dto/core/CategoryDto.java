package ru.nhp.api.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель категории продукта")
public class CategoryDto {
    @Schema(description = "Идентификатор категории", required = true, example = "1")
    private Long id;
    @NotBlank(message = "Поле названия категории не должно быть пустым")
    @Schema(description = "Название категории", required = true, example = "Все для дома")
    private String title;

    @Schema(description = "Id картинки категории", required = true, example = "1")
    private Long imageId;

    @NotBlank(message = "Описание категории не должно быть пустым")
    @Schema(description = "Описание категории", required = true, example = "Замечательная категория")
    private String description;

    @Override
    public String toString() {
        return title;
    }
}
