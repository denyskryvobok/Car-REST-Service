package com.foxminded.car_rest_service.exceptions.handlers;

import com.foxminded.car_rest_service.exceptions.custom.ManufacturerAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.ManufacturerNotFoundException;
import com.foxminded.car_rest_service.exceptions.response.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class ManufacturerExceptionAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(ManufacturerNotFoundException.class)
    public ResponseEntity<ErrorResponse> handelManufacturersNotFoundException(ManufacturerNotFoundException e) {
        log.info("HandelManufacturersNotFoundException started");

        HttpStatus httpStatus = HttpStatus.NOT_FOUND;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ManufacturerAlreadyExistException.class)
    public ResponseEntity<ErrorResponse> handelManufacturerAlreadyExistException(ManufacturerAlreadyExistException e) {
        log.info("HandelManufacturerAlreadyExistException started");

        HttpStatus httpStatus = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = new ErrorResponse(httpStatus.value(), e.getMessage());

        return ResponseEntity.status(httpStatus).body(errorResponse);
    }
}
