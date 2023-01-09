package com.foxminded.car_rest_service.openAPI;

public class OpenApiExamples {

    public static final String CATEGORY_REQUEST_BODY_WITH_NULL =
            "{\n" +
            "  \"id\": null,\n" +
            "  \"category\": \"SUV1992\"\n" +
            "}";

    public static final String DEFAULT_ERROR = "{\n" +
            "  \"data\": null,\n" +
            "  \"massage\": \"Error massage\"\n" +
            "}";
    public static final String VALIDATION_ERROR_DEFAULT = "{\n" +
            "  \"violations\": [\n" +
            "    {\n" +
            "      \"fieldName\": \"string\",\n" +
            "      \"message\": \"string\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    public static final String MODEL_REQUEST_BODY_WITH_NULL =
            "{\n" +
            "  \"id\": null,\n" +
            "  \"category\": \"Suburban 2500\"\n" +
            "}";
    public static final String MANUFACTURER_REQUEST_BODY_WITH_NULL =
            "{\n" +
            "  \"id\": null,\n" +
            "  \"manufacturer\": \"Cadillac\",\n" +
            "  \"year\": \"2021\"\n" +
            "}";
}
