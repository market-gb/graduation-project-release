package ru.nhp.api.exceptions.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Коды ошибок основного сервиса", example = "PRODUCT_NOT_FOUND")
public enum ServiceErrors {
    NOT_FOUND,
    VALIDATION_ERRORS,
    INVALID_PARAMS,
    INCORRECT_TOKEN,
    SERVICE_UNAVAILABLE
}
