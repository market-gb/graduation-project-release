package ru.nhp.api.dto.core;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

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
    @Size(min = 5, message = "Название категории должно быть не короче 5 символов")
    @Schema(description = "Название категории", required = true, example = "Категория№1")
    private String title;

    @NotBlank(message = "Поле пути к картинке категории не должно быть пустым")
    @Schema(description = "Путь к картинке категории", required = true, maxLength = 255, minLength = 3, example = "img/1.png")
    private String pathname;

    @NotBlank(message = "Описание категории не должно быть пустым")
    @Size(min = 5, message = "Описание категории должно быть не короче 5 символов")
    @Schema(description = "Описание категории", required = true, maxLength = 255, minLength = 3, example = "Замечательная категория")
    private String description;

    @Override
    public String toString() {
        return title;
    }
}
