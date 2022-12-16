package com.foxminded.car_rest_service.exceptions.custom;

public class CarNotFoundException extends RuntimeException{
    public CarNotFoundException(String massage) {
        super(massage);
    }
}
