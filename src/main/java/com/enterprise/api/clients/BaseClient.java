package com.enterprise.api.clients;

import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public abstract class BaseClient {

    protected Response get(RequestSpecification spec, String path) {
        return given().spec(spec).when().get(path);
    }

    protected Response get(RequestSpecification spec, String path, Map<String, ?> pathOrQueryParams) {
        return given().spec(spec).pathParams(pathOrQueryParams).when().get(path);
    }

    protected Response post(RequestSpecification spec, String path, Object body) {
        return given().spec(spec).body(body).when().post(path);
    }

    protected Response put(RequestSpecification spec, String path, Object body) {
        return given().spec(spec).body(body).when().put(path);
    }

    protected Response patch(RequestSpecification spec, String path, Object body) {
        return given().spec(spec).body(body).when().patch(path);
    }

    protected Response delete(RequestSpecification spec, String path) {
        return given().spec(spec).when().delete(path);
    }
}

