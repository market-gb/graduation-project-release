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
@Schema(description = "Модель акций")
public class BannerDto {
    @Schema(description = "Идентификатор акции", required = true, example = "1")
    private Long id;
    @NotBlank(message = "Поле названия акции не должно быть пустым")
    @Schema(description = "Название акции", required = true, example = "1 Акция")
    private String title;

    @Schema(description = "Id картинки акции", required = true, example = "1")
    private Long imageId;

    @Override
    public String toString() {
        return title;
    }

}
