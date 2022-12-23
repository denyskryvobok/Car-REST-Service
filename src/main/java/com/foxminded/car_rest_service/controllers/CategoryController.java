package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import com.foxminded.car_rest_service.services.CategoryService;
import com.foxminded.car_rest_service.utils.Mappings;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_CATEGORIES)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryBasicDTO>> getAllCategories(Pageable pageable) {
        log.info("GetAllCategories started");

        return new ResponseEntity<>(categoryService.getAllCategories(pageable), HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_CATEGORY_BY_NAME)
    public ResponseEntity<CategoryDTO> getCategoryWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetCategoryWithCarsByName started with name: {}", name);

        return new ResponseEntity<>(categoryService.getCategoryWithCarsByName(name), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CategoryBasicDTO> createCategory(@Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("CreateCategory started with input: {}", categoryBasicDTO);

        return new ResponseEntity<>(categoryService.createCategory(categoryBasicDTO), HttpStatus.CREATED);
    }

    @PutMapping(Mappings.UPDATE_CATEGORY_BY_ID)
    public ResponseEntity<CategoryBasicDTO> updateCategory(@PathVariable("id") Long id,
                                                           @Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("UpdateModel started with id: {}, categoryBasicDTO: {}", id, categoryBasicDTO);

        return new ResponseEntity<>(categoryService.updateCategory(id, categoryBasicDTO), HttpStatus.OK);
    }

    @DeleteMapping(Mappings.DELETE_CATEGORY_BY_NAME)
    public ResponseEntity<?> deleteCategoryByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteCategoryByName started with name: {}", name);

        categoryService.deleteCategoryByName(name);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
