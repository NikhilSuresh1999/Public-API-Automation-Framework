package com.enterprise.api.tests.dummyjson;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.DummyJsonDataProviders;
import com.enterprise.api.models.dummyjson.LoginRequest;
import com.enterprise.api.models.dummyjson.LoginResponse;
import com.enterprise.api.models.dummyjson.UserResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("DummyJSON Microservice")
@Feature("Authentication & JWT Management")
public class DummyJsonAuthTests extends BaseTest {

    @Test(description = "Verify successful authentication returns valid JWT token and user profile")
    @Story("Positive Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginWithValidCredentials() {
        Response response = dummyAuthClient.login(
                ConfigurationManager.get().dummyJsonUsername(),
                ConfigurationManager.get().dummyJsonPassword()
        );

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        LoginResponse loginResp = response.as(LoginResponse.class);
        assertThat(loginResp.getAccessToken()).isNotBlank();
        assertThat(loginResp.getUsername()).isEqualTo(ConfigurationManager.get().dummyJsonUsername());
        assertThat(loginResp.getEmail()).isNotBlank();
        assertThat(loginResp.getId()).isPositive();
    }

    @Test(dataProvider = "invalidLoginCredentials", dataProviderClass = DummyJsonDataProviders.class,
            description = "Verify login failure with incorrect credentials")
    @Story("Negative Login")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginWithInvalidCredentials(String username, String password, int expectedStatus) {
        Response response = dummyAuthClient.login(username, password);
        AssertionUtils.assertStatusCode(response, expectedStatus);
        AssertionUtils.assertBodyContains(response, "message");
    }

    @Test(description = "Verify retrieving current user profile with valid Bearer token")
    @Story("Current User Profile")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetCurrentUserProfileWithToken() {
        // Ensure access token is active
        if (dummyAccessToken == null) {
            var login = dummyAuthClient.login(
                    ConfigurationManager.get().dummyJsonUsername(),
                    ConfigurationManager.get().dummyJsonPassword()
            );
            dummyAccessToken = login.jsonPath().getString("accessToken");
        }

        Response response = dummyAuthClient.getCurrentUser(dummyAccessToken);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getUsername()).isEqualTo(ConfigurationManager.get().dummyJsonUsername());
    }

    @Test(description = "Verify 401 Unauthorized when retrieving user profile without token")
    @Story("Security - Unauthorized Access")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetCurrentUserWithoutToken() {
        Response response = dummyAuthClient.getCurrentUser(null);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.UNAUTHORIZED);
    }

    @Test(description = "Verify 401 Unauthorized when retrieving profile with malformed token")
    @Story("Security - Invalid Token")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetCurrentUserWithInvalidToken() {
        Response response = dummyAuthClient.getCurrentUser("invalid.bearer.token.payload");
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.UNAUTHORIZED);
    }

    @Test(description = "Verify token refresh flow returns refreshed access token")
    @Story("Token Refresh")
    @Severity(SeverityLevel.NORMAL)
    public void testRefreshTokenFlow() {
        // 1. Login to get refresh token
        Response loginResp = dummyAuthClient.login(
                ConfigurationManager.get().dummyJsonUsername(),
                ConfigurationManager.get().dummyJsonPassword()
        );
        String refreshToken = loginResp.jsonPath().getString("refreshToken");

        if (refreshToken != null) {
            Response refreshResp = dummyAuthClient.refreshToken(refreshToken);
            AssertionUtils.assertStatusCode(refreshResp, HttpStatusCodes.OK);
            assertThat(refreshResp.jsonPath().getString("accessToken")).isNotBlank();
        }
    }
}

