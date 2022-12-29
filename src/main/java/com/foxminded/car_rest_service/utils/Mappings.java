package com.foxminded.car_rest_service.utils;

public class Mappings {

    public static final String API_V1_CARS = "/api/v1/cars";

    public static final String GET_CARS_BY_MANUFACTURER = "/get/manufacturer";

    public static final String GET_CARS_BY_MANUFACTURER_AND_YEAR = "/get/manufacturer/year";

    public static final String CREATE_CAR = "manufacturer/{manufacturer}/model/{model}/year/{year}";

    public static final String DELETE_CAR_BY_ID = "/delete/id/{id}";

    public static final String ADD_CAR_TO_CATEGORY = "add/{id}/category/{name}";

    public static final String DELETE_CAR_FROM_CATEGORY = "remove/{id}/category/{name}";


    public static final String API_V1_MODELS = "/api/v1/models";

    public static final String GET_MODEL_BY_NAME = "/get/name/{name}";

    public static final String UPDATE_MODEL_BY_ID = "/update/id/{id}";

    public static final String DELETE_MODEL_BY_NAME = "/delete/name/{name}";


    public static final String API_V1_CATEGORIES = "/api/v1/categories";

    public static final String GET_CATEGORY_BY_NAME = "/get/name/{name}";

    public static final String UPDATE_CATEGORY_BY_ID = "/update/id/{id}";

    public static final String DELETE_CATEGORY_BY_NAME = "/delete/name/{name}";


    public static final String API_V1_MANUFACTURERS = "/api/v1/manufacturers";

    public static final String GET_MANUFACTURER_BY_NAME = "/get/name/{name}";

    public static final String UPDATE_MANUFACTURER_BY_ID = "/update/by/{id}";

    public static final String DELETE_MANUFACTURER_BY_NAME_AND_YEAR = "/delete/name/{name}/year/{year}";

    public static final String DELETE_MANUFACTURER_BY_NAME = "/delete/name/{name}";
}
