package com.foxminded.car_rest_service.mapstruct.dto.manufacturer;

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
@Schema(name = "ManufacturerBasic", description = "Manufacturer basic object")
public class ManufacturerBasicDTO {
    @Schema(example = "1", description = "Manufacturer id")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(example = "Cadillac", description = "Manufacturer name")
    private String manufacturer;

    @NotNull
    @Schema(example = "2021", description = "Year of creation")
    private Integer year;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManufacturerBasicDTO that = (ManufacturerBasicDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
