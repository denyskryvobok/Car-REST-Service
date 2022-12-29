package com.foxminded.car_rest_service.mapstruct.dto.car;

import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.Objects;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CarWithoutCategoriesDTO {
    private Long id;

    @NotNull
    private ModelBasicDTO model;

    @NotNull
    private ManufacturerBasicDTO manufacturer;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarWithoutCategoriesDTO that = (CarWithoutCategoriesDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
