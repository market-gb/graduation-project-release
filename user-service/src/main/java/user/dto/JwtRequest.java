package user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "Модель запроса")
public class JwtRequest {
    @Schema(description = "Имя пользователя", required = true)
    private String username;
    @Schema(description = "Пароль", required = true)
    private String password;
}
