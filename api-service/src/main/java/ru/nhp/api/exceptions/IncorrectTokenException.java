package ru.nhp.api.exceptions;

public class IncorrectTokenException extends RuntimeException {
    public IncorrectTokenException(String message) {
        super(message);
    }
}
