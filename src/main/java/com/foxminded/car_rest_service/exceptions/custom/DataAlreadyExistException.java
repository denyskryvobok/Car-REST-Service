package com.foxminded.car_rest_service.exceptions.custom;

public class DataAlreadyExistException extends RuntimeException{

    public DataAlreadyExistException(String message) {
        super(message);
    }
}
