package ru.nhp.core.exceptions;

import lombok.Getter;
import org.springframework.validation.ObjectError;

import java.util.List;

@Getter
public class CoreValidationException extends RuntimeException {
    private final List<ObjectError> errors;

    public CoreValidationException(String message, List<ObjectError> errors) {
        super(message);
        this.errors = errors;
    }
}
