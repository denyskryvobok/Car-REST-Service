package com.foxminded.car_rest_service.exceptions.handlers;

import com.foxminded.car_rest_service.exceptions.custom.ModelAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.ModelNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ModelExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ModelNotFoundException.class)
    public ResponseEntity<ErrorResponse> handelModelNotFoundException(ModelNotFoundException e) {
        log.info("HandelModelNotFoundException started");

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ModelAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handelModelAlreadyExistException(ModelAlreadyExistException e) {
        log.info("HandelModelAlreadyExistException started");

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
