package com.foxminded.car_rest_service.exceptions.custom;


public class ManufacturerNotFoundException extends RuntimeException {
    public ManufacturerNotFoundException(String massage) {
        super(massage);
    }
}
