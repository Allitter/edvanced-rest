package com.epam.esm.exception;

import com.epam.esm.util.ResourceBundleMessage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.TypeMismatchException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.stream.Collectors;

@ControllerAdvice
public class ControllerExceptionHandler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final MessageSource messageSource;

    public ControllerExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionResponse> handleEntityNotFound(EntityNotFoundException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.ENTITY_NOT_FOUND, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), message);
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntityAlreadyExistsException.class)
    public ResponseEntity<ExceptionResponse> handleEntityExists(EntityAlreadyExistsException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.ENTITY_ALREADY_EXISTS, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<ExceptionResponse> handleValidationException(ValidationException e, Locale locale) {
        LOGGER.error(e);
        List<String> messages = e.getValidationFails()
                .values().stream()
                .map(validationFail -> messageSource.getMessage(validationFail, new Object[]{}, locale))
                .collect(Collectors.toList());

        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messages);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionResponse> handleParseException(HttpMessageNotReadableException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.BAD_REQUEST, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SortArgumentException.class)
    public ResponseEntity<ExceptionResponse> handleSortArgumentException(SortArgumentException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.UNKNOWN_SORT_ARGUMENT, new Object[]{e.getArgName()}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleSortMethodArgumentNotValidException(MethodArgumentNotValidException e, Locale locale) {
        LOGGER.error(e);
        List<String> messages = e.getBindingResult().getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .filter(Objects::nonNull)
                .map(messageKey -> messageSource.getMessage(messageKey, new Object[]{}, locale))
                .collect(Collectors.toList());

        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), messages);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
//    public ResponseEntity<ExceptionResponse> handleMethodArgumentException(MethodArgumentTypeMismatchException e, Locale locale) {
//        LOGGER.error(e);
//        String exceptionMessage = e.getCause().getMessage();
//        Object parameter = exceptionMessage.substring(exceptionMessage.indexOf('[') + 1, exceptionMessage.lastIndexOf(']'));
//        String message = messageSource.getMessage(ResourceBundleMessage.BAD_PARAMETER, new Object[]{parameter}, locale);
//        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

    @ExceptionHandler(TypeMismatchException.class)
    public ResponseEntity<ExceptionResponse> handleTypeMismatchException(TypeMismatchException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.BAD_REQUEST, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), message);
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ExceptionResponse> handleException(RuntimeException e, Locale locale) {
        LOGGER.error(e);
        String message = messageSource.getMessage(ResourceBundleMessage.INTERNAL_SERVER_ERROR, new Object[]{}, locale);
        ExceptionResponse response = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), message);
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
