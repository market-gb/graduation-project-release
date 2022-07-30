package ru.nhp.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nhp.api.exceptions.AppError;
import ru.nhp.api.exceptions.InvalidParamsException;
import ru.nhp.api.exceptions.ResourceNotFoundException;
import ru.nhp.api.exceptions.ValidationException;
import ru.nhp.api.exceptions.enums.ServiceErrors;

@ControllerAdvice
public class CoreServiceExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<AppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new AppError(ServiceErrors.NOT_FOUND.name(), e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchInvalidParamsException(InvalidParamsException e) {
        return new ResponseEntity<>(new AppError(ServiceErrors.INVALID_PARAMS.name(), e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<AppError> catchValidationException(ValidationException e) {
        return new ResponseEntity<>(new AppError(ServiceErrors.VALIDATION_ERRORS.name(), e.getErrors().toString()), HttpStatus.BAD_REQUEST);
    }

}
