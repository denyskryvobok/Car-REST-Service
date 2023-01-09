package com.foxminded.car_rest_service.mapstruct.dto.model;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutModelDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "Model", description = "Model object")
public class ModelDTO {

    @Schema(example = "1", description = "Model Id")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(example = "Suburban 2500", description = "Model name")
    private String model;

    private Set<CarWithoutModelDTO> cars;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelDTO modelDTO = (ModelDTO) o;

        return Objects.equals(id, modelDTO.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
