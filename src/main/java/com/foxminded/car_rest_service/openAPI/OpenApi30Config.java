package com.foxminded.car_rest_service.openAPI;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(title = "Car Service API", version = "1.0.0", description = "Car Manager"),
        servers = @Server(description = "Dev server", url = "http://localhost:8080")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.OAUTH2,
        bearerFormat = "JWT",
        scheme = "bearer",
        flows = @OAuthFlows(
                password = @OAuthFlow(
                        tokenUrl = "http://keycloak:8180/auth/realms/car-service-realm/protocol/openid-connect/token"
                )
        )
)
public class OpenApi30Config {
    @Bean
    public OpenAPI customOpenAPI(OpenAPIComponentsManager manager) {
        return new OpenAPI().components(new Components()
                .addParameters("paramCategoryName", manager.getPathParam("name", "SUV1992"))
                .addParameters("paramModelName", manager.getPathParam("name", "Suburban 2500"))
                .addParameters("paramManufacturerName", manager.getPathParam("name", "Cadillac"))
                .addParameters("paramManufacturerYear", manager.getPathParam("year", "2021"))
                .addParameters("paramManufacturer", manager.getPathParam("manufacturer", "Cadillac"))
                .addParameters("paramModel", manager.getPathParam("model", "Suburban 2500"))
                .addParameters("id", manager.getParamId())

                .addSchemas("Error", manager.getErrorSchema())
                .addSchemas("ValidationError", manager.getValidationErrorSchema())
                .addSchemas("Manufacturer", manager.getManufacturerSchema())

                .addResponses("NotFound", manager.getNotFoundResponse())
                .addResponses("ValidationErrorResponse", manager.getValidationErrorResponse())
        );
    }
}
