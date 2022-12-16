package com.foxminded.car_rest_service.exceptions.custom;

public class CarAlreadyExistException extends RuntimeException {
    public CarAlreadyExistException(String massage) {
        super(massage);
    }
}
