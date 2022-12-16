package com.foxminded.car_rest_service.exceptions.handlers;

import com.foxminded.car_rest_service.exceptions.custom.CategoryAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.CategoryNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CategoryExceptionsAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CategoryNotFoundException.class)
    public ResponseEntity<ErrorResponse> handelCategoryNotFoundException(CategoryNotFoundException e) {
        log.info("HandelCategoryNotFoundException started");

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(CategoryAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handelCategoryAlreadyExistException(CategoryAlreadyExistException e) {
        log.info("handelCategoryAlreadyExistException started");

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
