package com.foxminded.car_rest_service.services;

import com.foxminded.car_rest_service.dao.CategoryDAO;
import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.exceptions.custom.CategoryAlreadyExistException;
import com.foxminded.car_rest_service.exceptions.custom.CategoryNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static java.lang.String.format;


@Slf4j
@Service
public class CategoryServiceImp implements CategoryService {

    @Autowired
    private CategoryDAO categoryDAO;

    @Override
    @Transactional(readOnly = true)
    public List<Category> getAllCategories(Pageable pageable) {
        log.info("getAllCategories started");

        List<Category> categories = categoryDAO.findAllPage(pageable);

        if (categories.isEmpty()) {
            var exception = new CategoryNotFoundException("No category was found");
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        }

        return categories;
    }

    @Override
    @Transactional(readOnly = true)
    public Category getCategoryWithCarsByName(String name) {
        log.info("GetModelWithCarsByName started with name: {}", name);

        return categoryDAO.findCategoryWithCarsByName(name).orElseThrow(() -> {
            var exception = new CategoryNotFoundException("No category was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });
    }


    @Override
    public Category createCategory(Category category) {
        log.info("CreateCategory started");

        categoryDAO.findByName(category.getCategory()).ifPresent(c -> {
            var exception = new CategoryAlreadyExistException(format("Category with name(%s) already exist", c ));
            log.error("Exception occurred during request processing: ", exception);
            throw exception;
        });

        return categoryDAO.save(category);
    }

    @Override
    public Category updateCategory(Long id, Category categoryInput) {
        log.info("UpdateCategory started with id: {}, category: {}", id, categoryInput);

        Category category = categoryDAO.findById(id).orElseThrow(() -> {
            var exception = new CategoryNotFoundException("No category was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        category.setCategory(categoryInput.getCategory());

        return categoryDAO.save(category);
    }

    @Override
    public void deleteCategoryByName(String name) {
        log.info("DeleteCategoryByName started with name: {}", name);

        Category category  = categoryDAO.findByName(name).orElseThrow(() -> {
            var exception = new CategoryNotFoundException("No model was found");
            log.error("Exception occurred during request processing: ", exception);
            return exception;
        });

        categoryDAO.delete(category);
    }
}
