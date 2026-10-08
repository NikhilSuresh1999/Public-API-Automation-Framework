package com.enterprise.api.utils;

import com.enterprise.api.config.ConfigurationManager;
import io.qameta.allure.Step;
import io.restassured.module.jsv.JsonSchemaValidator;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;

public final class AssertionUtils {

    private AssertionUtils() {
        // Prevent instantiation
    }

    @Step("Assert response status code is {1}")
    public static void assertStatusCode(Response response, int expectedStatusCode) {
        if (response.getStatusCode() == 429 && response.getBody().asString().contains("rate_limit_exceeded")) {
            throw new org.testng.SkipException("External demo API daily rate limit (40 req/day) exceeded. Test safely skipped.");
        }
        Assertions.assertThat(response.getStatusCode())
                .as("Verify HTTP Status Code")
                .isEqualTo(expectedStatusCode);
    }

    @Step("Assert response time is within SLA (< {0} ms)")
    public static void assertResponseTimeWithinSla(Response response, long maxAllowedTimeMs) {
        Assertions.assertThat(response.getTime())
                .as("Verify Response Time within SLA threshold")
                .isLessThan(maxAllowedTimeMs);
    }

    public static void assertResponseTimeWithinDefaultSla(Response response) {
        assertResponseTimeWithinSla(response, ConfigurationManager.get().maxResponseTimeMs());
    }

    @Step("Assert response matches JSON schema: {1}")
    public static void assertJsonSchema(Response response, String schemaClasspath) {
        response.then().assertThat().body(JsonSchemaValidator.matchesJsonSchemaInClasspath(schemaClasspath));
    }

    @Step("Assert response body contains text: {1}")
    public static void assertBodyContains(Response response, String expectedText) {
        Assertions.assertThat(response.getBody().asString())
                .as("Verify Response body contains expected text")
                .contains(expectedText);
    }

    @Step("Assert response header {1} is present")
    public static void assertHeaderPresent(Response response, String headerName) {
        Assertions.assertThat(response.getHeader(headerName))
                .as("Verify header: " + headerName)
                .isNotNull();
    }
}

