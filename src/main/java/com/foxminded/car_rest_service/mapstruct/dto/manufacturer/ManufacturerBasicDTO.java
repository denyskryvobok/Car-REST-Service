package com.foxminded.car_rest_service.mapstruct.dto.manufacturer;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class ManufacturerBasicDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String manufacturer;

    @NotNull
    private Integer year;
}
