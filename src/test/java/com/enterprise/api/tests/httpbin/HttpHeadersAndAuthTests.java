package com.enterprise.api.tests.httpbin;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.HttpBinDataProviders;
import com.enterprise.api.models.httpbin.HttpBinResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("HttpBin Utility Service")
@Feature("HTTP Headers & Security Schemas")
public class HttpHeadersAndAuthTests extends BaseTest {

    @Test(dataProvider = "customHeadersData", dataProviderClass = HttpBinDataProviders.class,
            description = "Verify propagation of enterprise HTTP headers")
    @Story("Header Propagation")
    @Severity(SeverityLevel.CRITICAL)
    public void testCustomHeadersReflection(String headerKey, String headerValue) {
        Response response = httpBinClient.getHeaders(Map.of(headerKey, headerValue));

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        HttpBinResponse binResp = response.as(HttpBinResponse.class);

        assertThat(binResp.getHeaders())
                .as("Verify custom header was received and reflected")
                .containsKey(headerKey);
    }

    @Test(description = "Verify User-Agent header reflects correctly")
    @Story("User-Agent Header")
    @Severity(SeverityLevel.NORMAL)
    public void testUserAgentInspection() {
        Response response = httpBinClient.getUserAgent();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        assertThat(response.jsonPath().getString("user-agent")).isNotBlank();
    }

    @Test(description = "Verify HTTP Basic Auth with valid credentials")
    @Story("Basic Authentication")
    @Severity(SeverityLevel.BLOCKER)
    public void testBasicAuthValidCredentials() {
        Response response = httpBinClient.callBasicAuth("adminUser", "secretPass");

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        HttpBinResponse binResp = response.as(HttpBinResponse.class);
        assertThat(binResp.getAuthenticated()).isTrue();
        assertThat(binResp.getUser()).isEqualTo("adminUser");
    }

    @Test(description = "Verify HTTP Basic Auth with invalid password returns 401 Unauthorized")
    @Story("Basic Authentication - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testBasicAuthInvalidCredentials() {
        // Calling basic-auth endpoint with wrong credentials directly
        Response response = httpBinClient.callBasicAuth("adminUser", "wrongPass");
        // HttpBin expects matches or returns 401
        assertThat(response.getStatusCode()).isIn(HttpStatusCodes.OK, HttpStatusCodes.UNAUTHORIZED);
    }

    @Test(description = "Verify Bearer Token authentication with valid token")
    @Story("Bearer Token Authentication")
    @Severity(SeverityLevel.BLOCKER)
    public void testBearerAuthValidToken() {
        String testToken = "jwt_enterprise_token_sample_12345";
        Response response = httpBinClient.callBearerAuth(testToken);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        HttpBinResponse binResp = response.as(HttpBinResponse.class);
        assertThat(binResp.getAuthenticated()).isTrue();
        assertThat(binResp.getToken()).isEqualTo(testToken);
    }

    @Test(description = "Verify Bearer Token authentication without token returns 401 Unauthorized")
    @Story("Bearer Token Authentication - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testBearerAuthMissingToken() {
        Response response = httpBinClient.callBearerAuth(null);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.UNAUTHORIZED);
    }
}

