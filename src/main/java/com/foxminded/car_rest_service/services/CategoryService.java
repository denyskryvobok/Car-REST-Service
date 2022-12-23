package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<CategoryBasicDTO> getAllCategories(Pageable pageable);

    CategoryDTO getCategoryWithCarsByName(String name);

    CategoryBasicDTO createCategory(CategoryBasicDTO category);

    CategoryBasicDTO updateCategory(Long id, CategoryBasicDTO categoryInput);

    void deleteCategoryByName(String name);
}
