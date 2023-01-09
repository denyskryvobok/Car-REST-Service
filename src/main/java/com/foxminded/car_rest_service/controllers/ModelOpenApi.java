package com.foxminded.car_rest_service.controllers;

import com.foxminded.car_rest_service.exceptions.response.ResultModel;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelBasicDTO;
import com.foxminded.car_rest_service.mapstruct.dto.model.ModelDTO;
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

@Tag(name = "Models", description = "Models manager")
public interface ModelOpenApi {

    @Operation(summary = "Finds all models",
            description = "Returns list of models",
            tags = "Models",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with list of models",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            array = @ArraySchema(schema = @Schema(ref = "ModelBasic"))
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound")})
    ResponseEntity<ResultModel> getAllModels(@ParameterObject Pageable pageable);

    @Operation(summary = "Finds a model by name",
            description = "Returns the model",
            tags = "Models",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with model",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(implementation = ModelDTO.class)
                                    ),
                                    @SchemaProperty(
                                            name = "massage",
                                            schema = @Schema(type = "string", example = "null")
                                    )
                            },
                            mediaType = APPLICATION_JSON_VALUE)}),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")})
    ResponseEntity<ResultModel> getModelWithCarsByName(@NotBlank @Parameter(ref = "paramModelName", example = "Suburban 2500") String name);


    @Operation(summary = "Create a model by name",
            description = "Creates a model and returns the created model",
            tags = "Models",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the created model",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "ModelBasic")
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
            description = "Model request body",
            content = @Content(schema = @Schema(ref = "ModelBasic"),
                    examples = @ExampleObject(value = OpenApiExamples.MODEL_REQUEST_BODY_WITH_NULL)))
    ResponseEntity<ResultModel> createModel(@Valid ModelBasicDTO modelBasicDTO);

    @Operation(summary = "Updates a model by name",
            description = "Updates a model and returns the updated category",
            tags = "Models",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Success response with the updated model",
                    content = {@Content(schema = @Schema(type = "object"),
                            schemaProperties = {
                                    @SchemaProperty(
                                            name = "data",
                                            schema = @Schema(ref = "ModelBasic")
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
            description = "Model request body",
            content = @Content(schema = @Schema(ref = "ModelBasic")))
    ResponseEntity<ResultModel> updateModel(@Parameter(ref = "id") Long id,
                                            @Valid @RequestBody ModelBasicDTO modelBasicDTO);

    @Operation(summary = "Deletes model by name with related cars",
            description = "Deletes a model",
            tags = "Models",
            security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    content = @Content(schema = @Schema),
                    description = "The resource was deleted successfully"),
            @ApiResponse(responseCode = "404", ref = "NotFound"),
            @ApiResponse(responseCode = "400", ref = "ValidationErrorResponse")
    })
    ResponseEntity<?> deleteModelByName(@NotBlank @Parameter(ref = "paramModelName") String name);
}
