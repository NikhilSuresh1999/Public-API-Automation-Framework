package com.enterprise.api.clients.httpbin;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class HttpBinClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getHttpBinSpec();

    @Step("Call status endpoint with code: {0}")
    public Response getStatus(int statusCode) {
        return given().spec(spec)
                .pathParam("code", statusCode)
                .when()
                .get(EndPoints.HTTPBIN_STATUS);
    }

    @Step("Get reflected request headers")
    public Response getHeaders(Map<String, String> customHeaders) {
        RequestSpecification req = given().spec(spec);
        if (customHeaders != null) {
            req.headers(customHeaders);
        }
        return req.when().get(EndPoints.HTTPBIN_HEADERS);
    }

    @Step("Call GET endpoint with query parameters: {0}")
    public Response callGet(Map<String, ?> queryParams) {
        return given().spec(spec)
                .queryParams(queryParams)
                .when()
                .get(EndPoints.HTTPBIN_GET);
    }

    @Step("Call POST endpoint with JSON body")
    public Response callPostJson(Object body) {
        return given().spec(spec)
                .body(body)
                .when()
                .post(EndPoints.HTTPBIN_POST);
    }

    @Step("Call PUT endpoint with JSON body")
    public Response callPutJson(Object body) {
        return given().spec(spec)
                .body(body)
                .when()
                .put(EndPoints.HTTPBIN_PUT);
    }

    @Step("Call PATCH endpoint with JSON body")
    public Response callPatchJson(Object body) {
        return given().spec(spec)
                .body(body)
                .when()
                .patch(EndPoints.HTTPBIN_PATCH);
    }

    @Step("Call DELETE endpoint")
    public Response callDelete() {
        return given().spec(spec)
                .when()
                .delete(EndPoints.HTTPBIN_DELETE);
    }

    @Step("Call basic auth with username: {0}")
    public Response callBasicAuth(String user, String passwd) {
        return given().spec(spec)
                .auth().basic(user, passwd)
                .pathParam("user", user)
                .pathParam("passwd", passwd)
                .when()
                .get(EndPoints.HTTPBIN_BASIC_AUTH);
    }

    @Step("Call bearer token endpoint with token: {0}")
    public Response callBearerAuth(String token) {
        RequestSpecification req = given().spec(spec);
        if (token != null) {
            req.header("Authorization", "Bearer " + token);
        }
        return req.when().get(EndPoints.HTTPBIN_BEARER);
    }

    @Step("Get client User-Agent")
    public Response getUserAgent() {
        return given().spec(spec)
                .when()
                .get(EndPoints.HTTPBIN_USER_AGENT);
    }
}

