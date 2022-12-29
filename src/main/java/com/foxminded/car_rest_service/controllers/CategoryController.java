package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
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

import static java.lang.String.format;

@Slf4j
@Validated
@RestController
@RequestMapping(Mappings.API_V1_CATEGORIES)
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ResultModel> getAllCategories(Pageable pageable) {
        log.info("GetAllCategories started");

        ResultModel resultModel = new ResultModel();
        List<CategoryBasicDTO> categories = categoryService.getAllCategories(pageable);

        if (categories.isEmpty()) {
            resultModel.setMassage("Categories not found");
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }
        resultModel.setData(categories);
        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @GetMapping(Mappings.GET_CATEGORY_BY_NAME)
    public ResponseEntity<ResultModel> getCategoryWithCarsByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("GetCategoryWithCarsByName started with name: {}", name);

        ResultModel resultModel = new ResultModel();
        CategoryDTO category = categoryService.getCategoryWithCarsByName(name);
        if (category == null) {
            resultModel.setMassage(format("Category with name(%s) not found", name));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }
        resultModel.setData(category);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResultModel> createCategory(@Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("CreateCategory started with input: {}", categoryBasicDTO);

        ResultModel resultModel = new ResultModel();

        CategoryBasicDTO category = categoryService.createCategory(categoryBasicDTO);

        if (category == null) {
            resultModel.setMassage(format("Category with name(%s) already exist", categoryBasicDTO.getCategory()));

            return new ResponseEntity<>(resultModel, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        resultModel.setData(category);

        return new ResponseEntity<>(resultModel, HttpStatus.CREATED);
    }

    @PutMapping(Mappings.UPDATE_CATEGORY_BY_ID)
    public ResponseEntity<ResultModel> updateCategory(@PathVariable("id") Long id,
                                                      @Valid @RequestBody CategoryBasicDTO categoryBasicDTO) {
        log.info("UpdateModel started with id: {}, categoryBasicDTO: {}", id, categoryBasicDTO);

        ResultModel resultModel = new ResultModel();

        CategoryBasicDTO category = categoryService.updateCategory(id, categoryBasicDTO);

        if (category == null) {
            resultModel.setMassage(format("Category with id(%d) wasn't found", id));

            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        resultModel.setData(category);

        return new ResponseEntity<>(resultModel, HttpStatus.OK);
    }

    @DeleteMapping(Mappings.DELETE_CATEGORY_BY_NAME)
    public ResponseEntity<?> deleteCategoryByName(@NotBlank @PathVariable(name = "name") String name) {
        log.info("DeleteCategoryByName started with name: {}", name);

        boolean isDeleted = categoryService.deleteCategoryByName(name);

        if (!isDeleted) {
            ResultModel resultModel = new ResultModel();
            resultModel.setMassage(format("Category with name(%s) wasn't found", name));
            return new ResponseEntity<>(resultModel, HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
