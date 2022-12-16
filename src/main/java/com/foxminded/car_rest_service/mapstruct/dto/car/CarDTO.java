package com.foxminded.car_rest_service.mapstruct.dto.car;

import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class CarDTO {

    private Long id;

    @NotNull
    private ModelBasicDTO model;

    @NotNull
    private ManufacturerBasicDTO manufacturer;

    private Set<CategoryBasicDTO> carCategories ;
}
