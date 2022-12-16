package com.foxminded.car_rest_service.mapstruct.dto.car;

import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
public class CarWithoutManufactureDTO {

    private Long id;

    private ModelBasicDTO model;

    private Set<CategoryBasicDTO> carCategories;

}
