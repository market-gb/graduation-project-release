package ru.nhp.api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
public class RoleDto {

    public void Role(String name){
        this.name = name;
        this.createdAt = LocalDateTime.now();
    }

    @Schema(description = "Идентификатор Роли", required = true, example = "1")
    private Long id;

    @NotBlank(message = "Поле названия пользователя не должно быть пустым")
    @Schema(description = "Название роли", required = true, maxLength = 255, minLength = 3, example = "Admin")
    private String name;

    @Schema(description = "Время создания роли", required = true, example = "2022.10.10")
    private LocalDateTime createdAt;

    @Schema(description = "Время изменения роли", required = true, example = "2022.10.10")
    private LocalDateTime updatedAt;
}
