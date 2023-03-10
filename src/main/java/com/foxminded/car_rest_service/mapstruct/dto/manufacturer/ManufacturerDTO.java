package com.foxminded.car_rest_service.mapstruct.dto.manufacturer;

import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutManufactureDTO;
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
@Schema(name = "Manufacturer", description = "Manufacturer object")
public class ManufacturerDTO {
    @Schema(example = "1")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(example = "Cadillac")
    private String manufacturer;

    @NotNull
    @Schema(example = "2021")
    private Integer year;

    private Set<CarWithoutManufactureDTO> cars;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ManufacturerDTO that = (ManufacturerDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
