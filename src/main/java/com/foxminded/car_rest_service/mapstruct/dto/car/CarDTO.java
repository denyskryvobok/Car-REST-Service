package com.foxminded.car_rest_service.mapstruct.dto.car;

import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarDTO {

    private Long id;

    @NotNull
    private ModelBasicDTO model;

    @NotNull
    private ManufacturerBasicDTO manufacturer;

    private Set<CategoryBasicDTO> carCategories ;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarDTO carDTO = (CarDTO) o;

        return Objects.equals(id, carDTO.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
