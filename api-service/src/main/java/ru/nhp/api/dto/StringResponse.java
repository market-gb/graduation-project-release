package ru.nhp.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public class StringResponse {
    @Schema(description = "Идентификатор корзины", required = true, example = "6548643218859643215215")
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public StringResponse(String value) {
        this.value = value;
    }

    public StringResponse() {
    }
}
