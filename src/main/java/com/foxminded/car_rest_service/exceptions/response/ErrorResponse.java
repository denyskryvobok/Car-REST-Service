package com.foxminded.car_rest_service.exceptions.response;

import lombok.Data;

@Data
public class ErrorResponse {
    private final Integer status;
    private final String massage;
}
