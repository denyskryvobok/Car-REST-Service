package com.foxminded.car_rest_service.exceptions.response;

import lombok.Data;

@Data
public class Violation {
    private final String fieldName;

    private final String message;
}
