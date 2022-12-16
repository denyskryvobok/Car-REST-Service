package com.foxminded.car_rest_service.exceptions.custom;

public class CategoryAlreadyExistException extends RuntimeException {
    public CategoryAlreadyExistException(String massage) {
        super(massage);
    }
}
