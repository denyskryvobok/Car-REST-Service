package com.foxminded.car_rest_service.mapstruct.dto.model;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutModelDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class ModelDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String model;

    private Set<CarWithoutModelDTO> cars;
}
