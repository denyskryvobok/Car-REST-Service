package com.foxminded.car_rest_service.mapstruct.dto.category;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
public class CategoryBasicDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String category;
}
