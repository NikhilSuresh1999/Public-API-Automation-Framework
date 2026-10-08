package com.enterprise.api.tests.booker;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.BookerDataProviders;
import com.enterprise.api.models.booker.AuthRequest;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker Microservice")
@Feature("Authentication & Token Generation")
public class BookerAuthTests extends BaseTest {

    @Test(description = "Verify successful token creation with valid credentials")
    @Story("Positive Authentication")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Verify that valid admin credentials return 200 OK and an alphanumeric token")
    public void testAuthWithValidCredentials() {
        Response response = bookerAuthClient.createToken("admin", "password123");

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        String token = response.jsonPath().getString("token");
        assertThat(token)
                .as("Verify generated auth token is not empty")
                .isNotBlank()
                .hasSizeGreaterThanOrEqualTo(10);
    }

    @Test(dataProvider = "authInvalidCredentials", dataProviderClass = BookerDataProviders.class,
            description = "Verify authentication failure with invalid or empty credentials")
    @Story("Negative Authentication")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verify that bad credentials return reason 'Bad credentials'")
    public void testAuthWithInvalidCredentials(String username, String password, String expectedReason) {
        Response response = bookerAuthClient.createToken(username, password);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        String reason = response.jsonPath().getString("reason");
        assertThat(reason)
                .as("Verify error reason message")
                .isEqualTo(expectedReason);

        String token = response.jsonPath().getString("token");
        assertThat(token).as("Verify token is null for failed authentication").isNull();
    }

    @Test(description = "Verify authentication behavior with extra unexpected fields")
    @Story("Security & Boundary")
    @Severity(SeverityLevel.NORMAL)
    public void testAuthWithExtraFieldsInPayload() {
        String payload = "{\"username\":\"admin\",\"password\":\"password123\",\"extraField\":\"maliciousValue\"}";
        Response response = bookerAuthClient.createTokenRaw(payload);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        String token = response.jsonPath().getString("token");
        assertThat(token).as("Token should still be generated when extra fields are passed").isNotBlank();
    }

    @Test(description = "Verify authentication with malformed JSON body")
    @Story("Security & Malformed Payloads")
    @Severity(SeverityLevel.NORMAL)
    public void testAuthWithMalformedJson() {
        String malformedJson = "{\"username\":\"admin\",\"password\":}";
        Response response = bookerAuthClient.createTokenRaw(malformedJson);

        // API should reject malformed JSON or return 400/500/200 Bad credentials
        assertThat(response.getStatusCode())
                .as("Verify status code is 200 (handled with Bad credentials) or 400")
                .isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST, HttpStatusCodes.INTERNAL_SERVER_ERROR);
    }

    @Test(description = "Verify health check ping endpoint")
    @Story("Health Check")
    @Severity(SeverityLevel.MINOR)
    public void testPingHealthCheck() {
        Response response = bookingClient.ping();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
    }
}

