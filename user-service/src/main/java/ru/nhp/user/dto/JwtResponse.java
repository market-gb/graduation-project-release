package ru.nhp.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Schema(description = "Модель ответа для передачи токена")
public class JwtResponse {
    @Schema(description = "токен авторизации", required = true)
    private String token;
    @Schema(description = "Служебный список", required = true, example = "{Example1, Example2}")
    private List<?> list;
}
