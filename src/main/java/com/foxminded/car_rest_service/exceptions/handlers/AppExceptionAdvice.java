package com.foxminded.car_rest_service.exceptions.handlers;

import com.foxminded.car_rest_service.exceptions.custom.DataAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.DataNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.ValidationErrorResponse;
import com.foxminded.car_rest_service.exceptions.response.Violation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;

@Slf4j
@RestControllerAdvice
public class AppExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(DataNotFoundException.class)
    public ResponseEntity<ErrorResponse> handelDataNotFoundException(DataNotFoundException e) {
        log.info("HandelDataNotFoundException started");

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(DataAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handelDataAlreadyExistException(DataAlreadyExistException e) {
        log.info("HandelDataAlreadyExistException started");

        HttpStatus httpStatus = HttpStatus.UNPROCESSABLE_ENTITY;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class)
    public ValidationErrorResponse handleConstraintViolationException(ConstraintViolationException e) {
        log.info("HandleConstraintViolationException started");

        ValidationErrorResponse error = new ValidationErrorResponse();
        for (var violation : e.getConstraintViolations()) {
            error.getViolations().add(new Violation(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        return error;
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ValidationErrorResponse handelMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.info("HandelMethodArgumentNotValidException started");

        ValidationErrorResponse error = new ValidationErrorResponse();
        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            error.getViolations().add(new Violation(fieldError.getField(), fieldError.getDefaultMessage()));
        }

        return error;
    }
}
