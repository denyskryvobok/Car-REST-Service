package com.foxminded.car_rest_service.exceptions.custom;

public class ManufacturerAlreadyExistException extends RuntimeException {

    public ManufacturerAlreadyExistException(String massage) {
        super(massage);
    }
}
