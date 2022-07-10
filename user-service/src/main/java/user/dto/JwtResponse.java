package user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@Schema(description = "Модель ответа для передачи токена")
public class JwtResponse {
    @Schema(description = "токен авторизации", required = true)
    private String token;
}
