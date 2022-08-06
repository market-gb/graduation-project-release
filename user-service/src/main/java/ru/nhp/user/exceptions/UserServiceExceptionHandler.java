package ru.nhp.user.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.IncorrectTokenException;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.SecondConfirmationException;
import ru.nhp.api.exceptions.WaitingConfirmException;
import ru.nhp.user.enums.AuthServiceMessage;
import ru.nhp.api.exceptions.enums.ServiceErrors;

@ControllerAdvice
@Slf4j
public class UserServiceExceptionHandler {
    @ExceptionHandler
    public ResponseEntity<AppError> catchBadCredentialsException(BadCredentialsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(ServiceErrors.INVALID_PARAMS.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchInvalidParamsException(InvalidParamsException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(ServiceErrors.INVALID_PARAMS.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchWaitingConfirmException(WaitingConfirmException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(AuthServiceMessage.WAITING_FOR_EMAIL_CONFIRMATION.name(), e.getMessage()), HttpStatus.CHECKPOINT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchIncorrectTokenException(IncorrectTokenException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(ServiceErrors.INCORRECT_TOKEN.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchSecondConfirmationException(SecondConfirmationException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(AuthServiceMessage.SECOND_CONFIRMATION_EMAIL.name(), e.getMessage()), HttpStatus.CHECKPOINT);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        log.error(e.getMessage(), e);
        return new ResponseEntity<>(new AppError(ServiceErrors.NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }
}