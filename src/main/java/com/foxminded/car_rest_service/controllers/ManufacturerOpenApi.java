package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.manufacturer.ManufacturerBasicDTO;
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

@Tag(name = "Manufacturers", description = "Manufacturer management")
public interface ManufacturerOpenApi {

    @Operation(summary = "Finds all unique manufacturers",
            description = "Returns list of manufacturers names",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of manufacturers",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(type = "string", example = "Cadillac"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound")})
    ResponseEntity<ResultModel> getAllUniqueManufacturers(@ParameterObject Pageable pageable);

    @Operation(summary = "Finds manufacturers by name",
            description = "Returns the model",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with lis of manufacturers",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "Manufacturer"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> getAllManufacturersByName(@NotBlank @Parameter(ref = "paramManufacturerName") String name,
                                                          @ParameterObject Pageable pageable);

    @Operation(summary = "Create a manufacturer by name and year",
            description = "Creates a manufacturer and returns the created manufacturer",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the created manufacturer",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "ManufacturerBasic")
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
            description = "Manufacturer request body",
            content = @Content(schema = @Schema(ref = "ManufacturerBasic"),
                    examples = @ExampleObject(value = OpenApiExamples.MANUFACTURER_REQUEST_BODY_WITH_NULL)))
    ResponseEntity<ResultModel> createManufacturer(@Valid ManufacturerBasicDTO manufacturerDTO);

    @Operation(summary = "Updates a manufacturer",
            description = "Updates a manufacturer and returns the updated manufacturer",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated manufacturer",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "ManufacturerBasic")
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
            description = "Manufacturer request body",
            content = @Content(schema = @Schema(ref = "Manufacturer")))
    ResponseEntity<ResultModel> updateManufacturer(@Parameter(ref = "id") Long id,
                                                   @Valid ManufacturerBasicDTO manufacturerBasicDTO);

    @Operation(summary = "Deletes a manufacturer by name and year with related cars",
            description = "Deletes a manufacturer",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema),
                    description = "The resource was deleted successfully"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")
    })
    ResponseEntity<?> deleteManufacturerByNameAndYear(@NotBlank @Parameter(ref = "paramManufacturerName") String name,
                                                      @Parameter(ref = "paramManufacturerYear") Integer year);

    @Operation(summary = "Deletes manufacturers by name with related cars",
            description = "Deletes manufacturers",
            tags = "Manufacturers",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema),
                    description = "The resource was deleted successfully"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")
    })
    ResponseEntity<?> deleteAllManufacturerByName(@NotBlank @Parameter(ref = "paramManufacturerName") String name);
}
