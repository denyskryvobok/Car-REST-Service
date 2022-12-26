package com.foxminded.car_rest_service.exceptions.response;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResultModel {
    private Object data;

    private String massage;
}
