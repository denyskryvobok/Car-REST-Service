package com.foxminded.car_rest_service.exceptions.custom;

public class ModelNotFoundException extends RuntimeException {

    public ModelNotFoundException(String massage) {
        super(massage);
    }
}
