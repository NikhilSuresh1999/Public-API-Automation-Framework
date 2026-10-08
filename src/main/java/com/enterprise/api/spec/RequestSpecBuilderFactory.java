package com.enterprise.api.spec;

import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.filters.CustomLoggingFilter;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.specification.RequestSpecification;

public final class RequestSpecBuilderFactory {

    private RequestSpecBuilderFactory() {
        // Prevent instantiation
    }

    private static RestAssuredConfig getDefaultConfig() {
        return RestAssuredConfig.config()
                .httpClient(HttpClientConfig.httpClientConfig()
                        .setParam("http.connection.timeout", 20000)
                        .setParam("http.socket.timeout", 20000));
    }

    public static RequestSpecification getBookerSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.get().bookerBaseUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setConfig(getDefaultConfig())
                .addFilter(new AllureRestAssured())
                .addFilter(new CustomLoggingFilter())
                .build();
    }

    public static RequestSpecification getDummyJsonSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.get().dummyJsonBaseUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setConfig(getDefaultConfig())
                .addFilter(new AllureRestAssured())
                .addFilter(new CustomLoggingFilter())
                .build();
    }

    public static RequestSpecification getReqresSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.get().reqresBaseUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setConfig(getDefaultConfig())
                .addFilter(new AllureRestAssured())
                .addFilter(new CustomLoggingFilter())
                .build();
    }

    public static RequestSpecification getHttpBinSpec() {
        return new RequestSpecBuilder()
                .setBaseUri(ConfigurationManager.get().httpbinBaseUrl())
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .setConfig(getDefaultConfig())
                .addFilter(new AllureRestAssured())
                .addFilter(new CustomLoggingFilter())
                .build();
    }
}
