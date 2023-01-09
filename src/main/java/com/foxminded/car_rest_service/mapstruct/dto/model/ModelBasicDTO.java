package com.foxminded.car_rest_service.mapstruct.dto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "ModelBasic", description = "Basic model object")
public class ModelBasicDTO {

    @Schema(example = "1", description = "Id of model")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(example = "Suburban 2500", description = "Model name")
    private String model;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModelBasicDTO that = (ModelBasicDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
