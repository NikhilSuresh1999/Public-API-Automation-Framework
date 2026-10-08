package com.enterprise.api.spec;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.ResponseSpecification;
import org.hamcrest.Matchers;

public final class ResponseSpecBuilderFactory {

    private ResponseSpecBuilderFactory() {
        // Prevent instantiation
    }

    public static ResponseSpecification getSuccessSpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectContentType(ContentType.JSON)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }

    public static ResponseSpecification getStatusCodeOnlySpec(int expectedStatusCode) {
        return new ResponseSpecBuilder()
                .expectStatusCode(expectedStatusCode)
                .expectResponseTime(Matchers.lessThan(5000L))
                .build();
    }
}

