package com.foxminded.car_rest_service.openAPI;

import com.foxminded.car_rest_service.exceptions.response.Violation;
import com.foxminded.car_rest_service.mapstruct.dto.car.CarWithoutManufactureDTO;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.IntegerSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.ObjectSchema;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.stereotype.Component;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Component
public class OpenAPIComponentsManager {

    public Schema<?> getErrorSchema() {
        return new ObjectSchema()
                .addProperty("data", new ObjectSchema().example(null).description("data should be null"))
                .addProperty("massage", new StringSchema().example("Error massage"));
    }

    public Schema<?> getValidationErrorSchema() {
        return new Schema<>()
                .addProperty("violations",
                        new ArraySchema().items(converClassToSchema(Violation.class)));
    }

    public ApiResponse getNotFoundResponse() {
        return new ApiResponse().description("The specified resource was not found")
                .content(new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<>().$ref("Error"))));

    }

    public ApiResponse getValidationErrorResponse() {
        return new ApiResponse().description("Validation Error")
                .content(new Content().addMediaType(APPLICATION_JSON_VALUE,
                        new MediaType().schema(new Schema<>().$ref("ValidationError"))));
    }

    public Parameter getParamId() {
        return new Parameter().name("id")
                .description("Input id")
                .in("path")
                .required(true)
                .schema(new Schema<>().type("integer").format("int64").example(1));
    }

    public Parameter getPathParam(String name, String example) {
        return new Parameter().name(name)
                .in("path")
                .required(true)
                .description("Input " + name)
                .schema(new StringSchema())
                .example(example);
    }

    public Schema<?> getManufacturerSchema() {
        return new ObjectSchema()
                .description("Manufacturer object")
                .addProperty("id", new IntegerSchema().format("int64").example(1).description("Manufacturer id"))
                .addProperty("manufacturer", new StringSchema().example("Cadillac").description("Manufacturer name"))
                .addProperty("year", new IntegerSchema().example("2021").description("Year of creation"))
                .addProperty("cars", new ArraySchema().items(converClassToSchema(CarWithoutManufactureDTO.class)));
    }

    private Schema<?> converClassToSchema(Class<?> inputClass) {
        return ModelConverters.getInstance().readAllAsResolvedSchema(inputClass).schema;
    }
}
