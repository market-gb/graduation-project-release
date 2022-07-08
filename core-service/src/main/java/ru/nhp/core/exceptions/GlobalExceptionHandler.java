package ru.nhp.core.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import ru.nhp.api.exceptions.ResourceNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<CoreAppError> catchResourceNotFoundException(ResourceNotFoundException e) {
        return new ResponseEntity<>(new CoreAppError(CoreAppError.ServiceErrors.PRODUCT_NOT_FOUND, e.getMessage()), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity<CoreAppError> catchInvalidParamsException(InvalidParamsException e) {
        return new ResponseEntity<>(new CoreAppError(CoreAppError.ServiceErrors.INVALID_PARAMS, e.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CoreAppError> catchValidationException(CoreValidationException e) {
        return new ResponseEntity<>(new CoreAppError(CoreAppError.ServiceErrors.VALIDATION_ERRORS, e.getMessage(), e.getErrors()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<CoreAppError> catchCartServiceIntegrationException(CartServiceIntegrationException e) {
        return new ResponseEntity<>(new CoreAppError(CoreAppError.ServiceErrors.CART_SERVICE_IS_BROKEN, e.getMessage()), HttpStatus.NOT_FOUND);
    }
}
