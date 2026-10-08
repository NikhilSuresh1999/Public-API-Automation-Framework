package com.enterprise.api.clients.booker;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.booker.AuthRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BookerAuthClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getBookerSpec();

    @Step("Authenticate with credentials: username={0}")
    public Response createToken(String username, String password) {
        AuthRequest body = AuthRequest.builder()
                .username(username)
                .password(password)
                .build();
        return given().spec(spec)
                .body(body)
                .when()
                .post(EndPoints.BOOKER_AUTH);
    }

    @Step("Authenticate with payload object")
    public Response createToken(AuthRequest body) {
        return given().spec(spec)
                .body(body)
                .when()
                .post(EndPoints.BOOKER_AUTH);
    }

    @Step("Authenticate with raw payload string")
    public Response createTokenRaw(String rawJson) {
        return given().spec(spec)
                .body(rawJson)
                .when()
                .post(EndPoints.BOOKER_AUTH);
    }

    public String getDefaultToken() {
        Response response = createToken(
                ConfigurationManager.get().bookerUsername(),
                ConfigurationManager.get().bookerPassword()
        );
        return response.jsonPath().getString("token");
    }
}

