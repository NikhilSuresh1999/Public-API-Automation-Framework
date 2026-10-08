package com.enterprise.api.clients.dummyjson;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class UserClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getDummyJsonSpec();

    @Step("Get all users with default pagination")
    public Response getAllUsers() {
        return given().spec(spec)
                .when()
                .get(EndPoints.DUMMY_USERS);
    }

    @Step("Get users with query parameters: {0}")
    public Response getUsersWithParams(Map<String, ?> params) {
        return given().spec(spec)
                .queryParams(params)
                .when()
                .get(EndPoints.DUMMY_USERS);
    }

    @Step("Get user by ID: {0}")
    public Response getUserById(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(EndPoints.DUMMY_USER_BY_ID);
    }

    @Step("Search users with query: {0}")
    public Response searchUsers(String query) {
        return given().spec(spec)
                .queryParam("q", query)
                .when()
                .get(EndPoints.DUMMY_USER_SEARCH);
    }

    @Step("Filter users with key: {0} and value: {1}")
    public Response filterUsers(String key, String value) {
        return given().spec(spec)
                .queryParam("key", key)
                .queryParam("value", value)
                .when()
                .get(EndPoints.DUMMY_USER_FILTER);
    }
}

