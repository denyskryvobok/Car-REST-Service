package com.foxminded.car_rest_service.mapstruct.dto.model;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class ModelBasicDTO {

    private Long id;

    @NotBlank
    private String model;

}
