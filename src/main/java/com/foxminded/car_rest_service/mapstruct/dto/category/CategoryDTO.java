package com.foxminded.car_rest_service.mapstruct.dto.category;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Getter
@Setter
public class CategoryDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String category;

    private Set<CarWithoutCategoriesDTO> cars;
}
