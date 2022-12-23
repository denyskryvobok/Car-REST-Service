package com.foxminded.car_rest_service.exceptions.custom;

public class DataNotFoundException extends RuntimeException {

    public DataNotFoundException(String message) {
        super(message);
    }
}
