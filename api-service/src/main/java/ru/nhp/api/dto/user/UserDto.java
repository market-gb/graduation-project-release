package ru.nhp.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Модель пользователя")
public class UserDto {
    @Schema(description = "Идентификатор пользователя", required = true, example = "1")
    private Long id;

    @NotBlank(message = "Поле названия пользователя не должно быть пустым")
    @Schema(description = "Название товара", required = true, maxLength = 255, minLength = 3, example = "Товар#1")
    private String username;

    @NotBlank(message = "Описание товара не должно быть пустым")
    @Schema(description = "Описание товара", required = true, maxLength = 255, minLength = 3, example = "Замечательный товар")
    private String password;

    @NotBlank(message = "Описание товара не должно быть пустым")
    @Schema(description = "Описание товара", required = true, maxLength = 255, minLength = 3, example = "Замечательный товар")
    private String email;

    @NotNull(message = "Должна быть указана хотя бы одна категория")
    @Schema(description = "Идентификаторы категорий товара", required = true, example = "{1, 2}")
    private Boolean enabled;

    private Set<String> roles;

    @Schema(description = "Время создания товара", required = true, example = "2022.10.10")
    private LocalDateTime createdAt;

    @Schema(description = "Время изменения товара", required = true, example = "2022.10.10")
    private LocalDateTime updatedAt;

}
