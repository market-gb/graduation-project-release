package ru.nhp.api.exceptions;

public class WaitingConfirmException extends RuntimeException {
    public WaitingConfirmException(String message) {
        super(message);
    }
}
