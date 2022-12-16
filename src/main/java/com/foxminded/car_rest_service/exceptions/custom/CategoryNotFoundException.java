package com.foxminded.car_rest_service.exceptions.custom;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String massage) {
        super(massage);
    }
}
