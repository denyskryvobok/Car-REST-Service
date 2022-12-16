package com.foxminded.car_rest_service.exceptions.response;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse {
    private final List<Violation> violations = new ArrayList<>();
}
