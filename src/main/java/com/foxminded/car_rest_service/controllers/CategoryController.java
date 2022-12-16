package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.entities.Category;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import com.foxminded.car_rest_service.mapstruct.mapper.CategoryMapper;
import com.foxminded.car_rest_service.services.CategoryService;
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

import static java.util.stream.Collectors.toList;

@Slf4j
@Validated
@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryMapper mapper;

    @GetMapping
    public List<CategoryBasicDTO> getAllCategories(Pageable pageable) {
        log.info("GetAllCategories started");

        return categoryService.getAllCategories(pageable).stream()
                .map(category -> mapper.categoryToCategoryBasicDTO(category))
                .collect(toList());
    }

    @GetMapping("{name}")
    public ResponseEntity<CategoryDTO> getCategoryWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetCategoryWithCarsByName started");

        return ResponseEntity.ok().body(mapper.categoryToCategoryDTO(categoryService.getCategoryWithCarsByName(name)));
    }

    @PostMapping
    public ResponseEntity<CategoryBasicDTO> createCategory(@Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("CreateCategory started with input: {}", categoryBasicDTO);

        Category category = categoryService.createCategory(mapper.categoryBasicToCategory(categoryBasicDTO));

        return ResponseEntity.status(HttpStatus.CREATED).body(mapper.categoryToCategoryBasicDTO(category));
    }

    @PutMapping("{id}")
    public ResponseEntity<CategoryBasicDTO> updateCategory(@PathVariable("id") Long id,
                                                           @Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("UpdateModel started with id: {}, categoryBasicDTO: {}", id, categoryBasicDTO);

        Category category = categoryService.updateCategory(id, mapper.categoryBasicToCategory(categoryBasicDTO));

        return ResponseEntity.ok().body(mapper.categoryToCategoryBasicDTO(category));
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<?> deleteCategoryByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteCategoryByName started with name: {}", name);

        categoryService.deleteCategoryByName(name);

        return ResponseEntity.noContent().build();
    }
}
