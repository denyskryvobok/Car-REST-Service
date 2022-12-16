package com.foxminded.car_rest_service.mapstruct.dto.manufacturer;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutManufactureDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class ManufacturerDTO {
    private Long id;

    @NotNull
    @NotBlank
    private String manufacturer;

    @NotNull
    private Integer year;

    private Set<CarWithoutManufactureDTO> cars;

}
