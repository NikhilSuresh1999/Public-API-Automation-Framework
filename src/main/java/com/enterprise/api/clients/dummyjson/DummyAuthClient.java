package com.enterprise.api.clients.dummyjson;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.dummyjson.LoginRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class DummyAuthClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getDummyJsonSpec();

    @Step("Login with username: {0}")
    public Response login(String username, String password) {
        LoginRequest req = LoginRequest.builder()
                .username(username)
                .password(password)
                .expiresInMins(30)
                .build();
        return given().spec(spec)
                .body(req)
                .when()
                .post(EndPoints.DUMMY_AUTH_LOGIN);
    }

    @Step("Login with request payload")
    public Response login(LoginRequest request) {
        return given().spec(spec)
                .body(request)
                .when()
                .post(EndPoints.DUMMY_AUTH_LOGIN);
    }

    @Step("Get current authenticated user profile using token")
    public Response getCurrentUser(String token) {
        RequestSpecification req = given().spec(spec);
        if (token != null) {
            req.header("Authorization", "Bearer " + token);
        }
        return req.when().get(EndPoints.DUMMY_AUTH_ME);
    }

    @Step("Refresh access token using refresh token")
    public Response refreshToken(String refreshToken) {
        return given().spec(spec)
                .body(Map.of("refreshToken", refreshToken, "expiresInMins", 30))
                .when()
                .post(EndPoints.DUMMY_AUTH_REFRESH);
    }
}

