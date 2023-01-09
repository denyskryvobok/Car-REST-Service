package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.category.CategoryDTO;
import com.foxminded.car_rest_service.openAPI.OpenApiExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.SchemaProperty;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springdoc.api.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Tag(name = "Categories", description = "Category management")
public interface CategoryOpenApi {

    @Operation(summary = "Finds all categories",
            description = "Returns list of categories",
            tags = "Categories",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of categories",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "CategoryBasic"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound")})
    ResponseEntity<ResultModel> getAllCategories(@ParameterObject Pageable pageable);

    @Operation(summary = "Finds a category by name",
            description = "Returns the category",
            tags = "Categories",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with category",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(implementation = CategoryDTO.class)
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> getCategoryWithCarsByName(@NotBlank
                                                          @Parameter(ref = "paramCategoryName") String name);

    @Operation(summary = "Create a category by name",
            description = "Creates a category and returns the created category",
            tags = "Categories",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the created category",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(implementation = CategoryBasicDTO.class)
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "422",
                    content = @Content(schema = @Schema, examples = {
                            @ExampleObject(description = "Input value already exists",
                                    name = "alreadyExistsExample",
                                    summary = "Input already exists",
                                    value = OpenApiExamples.DEFAULT_ERROR),
                            @ExampleObject(description = "Validation Error",
                                    name = "validationErrorExample",
                                    summary = "Validation error",
                                    value = OpenApiExamples.VALIDATION_ERROR_DEFAULT)
                    }))})
    @RequestBody(required = true,
            description = "Category request body",
            content = @Content(schema = @Schema(ref = "CategoryBasic"),
                    examples = @ExampleObject(value = OpenApiExamples.CATEGORY_REQUEST_BODY_WITH_NULL)))
    ResponseEntity<ResultModel> createCategory(@Valid CategoryBasicDTO categoryBasicDTO);

    @Operation(summary = "Updates a category by name",
            description = "Updates a category and returns the updated category",
            tags = "Categories",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated category",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "CategoryBasic")
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "422",
                    content = @Content(schema = @Schema, examples = {
                            @ExampleObject(description = "Input value already exists",
                                    name = "alreadyExistsExample",
                                    summary = "Input already exists",
                                    value = OpenApiExamples.DEFAULT_ERROR),
                            @ExampleObject(description = "Validation Error",
                                    name = "validationErrorExample",
                                    summary = "Validation error",
                                    value = OpenApiExamples.VALIDATION_ERROR_DEFAULT)
                    }))})
    @RequestBody(required = true,
            description = "Category request body",
            content = @Content(schema = @Schema(ref = "CategoryBasic")))
    ResponseEntity<ResultModel> updateCategory(@Parameter(ref = "id") Long id,
                                               @Valid CategoryBasicDTO categoryBasicDTO);

    @Operation(summary = "Deletes category by name",
            description = "Deletes a category",
            tags = "Categories",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema),
                    description = "The resource was deleted successfully"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")
    })
    ResponseEntity<?> deleteCategoryByName(@NotBlank @Parameter(ref = "paramCategoryName") String name);
}
