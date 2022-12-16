package com.foxminded.car_rest_service.exceptions.custom;

public class ModelAlreadyExistException extends RuntimeException {
    public ModelAlreadyExistException(String massage) {
        super(massage);
    }
}
