package ru.nhp.api.exceptions;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Сообщения ошибок сервисов", example = "Продукт не найден")
public class AppError {
    private String statusCode;
    private String message;
}
