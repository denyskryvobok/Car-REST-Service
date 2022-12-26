package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.CategoryDAO;
import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;
import static java.util.stream.Collectors.toList;


@Slf4j
@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Autowired
    private CategoryMapper mapper;

    @Override
    @Transactional(readOnly = true)
    public List<CategoryBasicDTO> getAllCategories(Pageable pageable) {
        log.info("GetAllCategories started");

        return categoryDAO.findAllPage(pageable).stream()
                .map(category -> mapper.categoryToCategoryBasicDTO(category))
                .collect(toList());
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryDTO getCategoryWithCarsByName(String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        Category category = categoryDAO.findCategoryWithCarsByName(name).orElseGet(() -> {
            log.error(format("Category with name(%s) wasn't found", name));
            return null;
        });

        return mapper.categoryToCategoryDTO(category);
    }


    @Override
    public CategoryBasicDTO createCategory(CategoryBasicDTO category) {
        log.info("CreateCategory started");

        Optional<Category> optional = categoryDAO.findByName(category.getCategory());
        if (optional.isPresent()) {
            return null;
        }
        return mapper.categoryToCategoryBasicDTO(categoryDAO.save(mapper.categoryBasicToCategory(category)));
    }

    @Override
    public CategoryBasicDTO updateCategory(Long id, CategoryBasicDTO categoryInput) {
        log.info("UpdateCategory started with id: {}, category: {}", id, categoryInput);

        return categoryDAO.findById(id)
                .map((category -> {
                    category.setCategory(categoryInput.getCategory());
                    return mapper.categoryToCategoryBasicDTO(categoryDAO.save(category));
                })).orElse(null);
    }

    @Override
    public boolean deleteCategoryByName(String name) {
        log.info("DeleteCategoryByName started with name: {}", name);

        return categoryDAO.findByName(name).map(category -> {
            categoryDAO.delete(category);
            return true;
        }).orElse(false);
    }
}
