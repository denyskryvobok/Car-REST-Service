package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.entities.Category;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    List<Category> getAllCategories(Pageable pageable);

    Category getCategoryWithCarsByName(String name);

    Category createCategory(Category category);

    Category updateCategory(Long id, Category categoryInput);

    void deleteCategoryByName(String name);
}
