package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarDTO;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutCategoriesDTO;
import com.foxminded.car_rest_service.openAPI.OpenApiExamples;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
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

@Tag(name = "Cars", description = "Car management")
public interface CarOpenApi {

    @Operation(summary = "Finds all cars",
            description = "Returns list of cars",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of cars",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "Car"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound")})
    ResponseEntity<ResultModel> getAllCars(@ParameterObject Pageable pageable);

    @Operation(summary = "Finds all cars by manufacturer",
            description = "Returns list of cars",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of cars",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "Car"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> getAllCarsByManufacturer(@NotBlank
                                                         @Parameter(
                                                                 name = "manufacturer",
                                                                 description = "Manufacturer name",
                                                                 required = true,
                                                                 in = ParameterIn.QUERY,
                                                                 schema = @Schema(type = "string", example = "Cadillac")
                                                         ) String manufacturer,
                                                         @ParameterObject Pageable pageable);

    @Operation(summary = "Finds all cars by manufacturer and min year",
            description = "Returns list of cars",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of cars",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "Car"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> getAllCarsByManufacturerAndMinYear(@NotBlank
                                                                   @Parameter(
                                                                           name = "manufacturer",
                                                                           description = "Manufacturer name",
                                                                           required = true,
                                                                           in = ParameterIn.QUERY,
                                                                           schema = @Schema(type = "string", example = "Cadillac")
                                                                   ) String manufacturer,
                                                                   @Parameter(
                                                                           name = "year",
                                                                           description = "Minimum year of creation",
                                                                           required = true,
                                                                           in = ParameterIn.QUERY,
                                                                           schema = @Schema(type = "string", example = "2021")
                                                                   ) Integer year,
                                                                   @ParameterObject Pageable pageable);

    @Operation(summary = "Create a car by manufacturer, model, and year",
            description = "Creates a car and returns the created car",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the created car",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(implementation = CarDTO.class)
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse"),
            @ApiResponse(responseCode = "422", description = "Input value already exists",
                    content = @Content(schema = @Schema, examples = @ExampleObject(OpenApiExamples.DEFAULT_ERROR)))})
    ResponseEntity<ResultModel> createCar(@NotBlank @Parameter(ref = "paramManufacturer") String manufacturer,
                                          @NotBlank @Parameter(ref = "paramModel") String model,
                                          @Parameter(ref = "paramManufacturerYear") Integer year);

    @Operation(summary = "Updates a car",
            description = "Updates a car and returns the updated car",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated car",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "CarWithoutCategories")
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
            description = "Car request body",
            content = @Content(schema = @Schema(ref = "CarWithoutCategories")))
    ResponseEntity<ResultModel> updateCar(@Valid CarWithoutCategoriesDTO carWithoutCategoriesDTO);

    @Operation(summary = "Deletes car by id",
            description = "Deletes a car",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema),
                    description = "The resource was deleted successfully"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")
    })
    ResponseEntity<?> deleteCarById(@Parameter(ref = "id") Long id);

    @Operation(summary = "Add car categories",
            description = "Add category to car and returns the updated car",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated car",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "Car")
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> addCarToCategory(@Parameter(ref = "id") Long id,
                                                 @NotBlank @Parameter(ref = "paramCategoryName") String name);

    @Operation(summary = "Delete car categories",
            description = "Delete category from car and returns the updated car",
            tags = "Cars",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated car",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "Car")
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> removeCarFromCategory(@Parameter(ref = "id") Long id,
                                                      @NotBlank @Parameter(ref = "paramCategoryName") String name);
}
