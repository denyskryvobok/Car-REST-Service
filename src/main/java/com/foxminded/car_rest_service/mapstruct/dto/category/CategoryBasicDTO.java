package com.foxminded.car_rest_service.mapstruct.dto.category;

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
@Schema(name = "CategoryBasic", description = "Category basic object")
public class CategoryBasicDTO {

    @Schema(example = "1", description = "Category id")
    private Long id;

    @NotNull
    @NotBlank
    @Schema(example = "SUV1992", description = "Category name")
    private String category;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CategoryBasicDTO that = (CategoryBasicDTO) o;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : 0;
    }
}
