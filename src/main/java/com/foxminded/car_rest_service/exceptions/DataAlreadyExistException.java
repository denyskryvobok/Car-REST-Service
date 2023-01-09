package com.foxminded.car_rest_service.exceptions;

public class DataAlreadyExistException extends RuntimeException {
    public DataAlreadyExistException(String massage) {
        super(massage);
    }
}
