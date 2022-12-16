package com.foxminded.car_rest_service.mapstruct.mapper;

import com.foxminded.car_rest_service.entities.CarCategoryInfo;
import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;

import java.util.HashSet;
import java.util.Set;

public interface MainMapper {

    default Set<CategoryBasicDTO> carCategoriesToCategoryDTO(Set<CarCategoryInfo> categoryInfo) {
        if (categoryInfo == null) {
            return null;
        }

        Set<CategoryBasicDTO> categoryDTO = new HashSet<>();

        for (CarCategoryInfo carCategory : categoryInfo) {
            Category category = carCategory.getCategory();
            CategoryBasicDTO categoryBasicDTO = new CategoryBasicDTO(category.getId(), category.getCategory());
            categoryDTO.add(categoryBasicDTO);
        }

        return categoryDTO;
    }
}
