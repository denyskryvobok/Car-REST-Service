package com.foxminded.car_rest_service.exceptions.custom;

public class CarAlreadyHasSuchCategoryException extends RuntimeException {
    public CarAlreadyHasSuchCategoryException(String massage) {
        super(massage);
    }
}
