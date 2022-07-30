package ru.nhp.user.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Коды ошибок сервиса аутентификации", example = "AUTH_SERVICE_INCORRECT_USERNAME_OR_PASSWORD")
public enum AuthServiceMessage {
    WAITING_FOR_EMAIL_CONFIRMATION,
    SECOND_CONFIRMATION_EMAIL
}
