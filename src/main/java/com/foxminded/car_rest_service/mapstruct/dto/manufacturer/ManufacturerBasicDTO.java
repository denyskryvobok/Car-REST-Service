package com.foxminded.car_rest_service.mapstruct.dto.manufacturer;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ManufacturerBasicDTO {

    private Long id;

    @NotNull
    @NotBlank
    private String manufacturer;

    @NotNull
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
