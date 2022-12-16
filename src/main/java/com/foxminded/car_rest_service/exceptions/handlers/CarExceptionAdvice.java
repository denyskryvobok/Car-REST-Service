package com.foxminded.car_rest_service.exceptions.handlers;

import com.foxminded.car_rest_service.exceptions.custom.CarAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.CarAlreadyHasSuchCategoryException;
import com.foxminded.car_rest_service.exceptions.custom.CarNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CarExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(CarNotFoundException.class)
    public ResponseEntity<ErrorResponse> handelCarNotFoundException(CarNotFoundException e) {
        log.info("HandelCarNotFoundException started");

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler({CarAlreadyExistException.class, CarAlreadyHasSuchCategoryException.class})
    public ResponseEntity<ErrorResponse> handelCarConflictExceptions(RuntimeException e) {
        log.info("HandelCarAlreadyExistException started");

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
