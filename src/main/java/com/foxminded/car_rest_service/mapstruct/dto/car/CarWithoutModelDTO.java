package com.foxminded.car_rest_service.mapstruct.dto.car;

import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(name = "CarWithoutModel", description = "Car without model object")
public class CarWithoutModelDTO {

    @Schema(example = "1", description = "Car id")
    private Long id;

    @NotNull
    private ManufacturerBasicDTO manufacturer;

    private Set<CategoryBasicDTO> carCategories;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CarWithoutModelDTO that = (CarWithoutModelDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
