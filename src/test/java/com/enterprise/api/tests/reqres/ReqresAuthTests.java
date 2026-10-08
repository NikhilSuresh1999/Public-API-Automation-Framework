package com.enterprise.api.tests.reqres;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.ReqresDataProviders;
import com.enterprise.api.models.reqres.ReqresAuthRequest;
import com.enterprise.api.models.reqres.ReqresAuthResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("ReqRes Mock Microservice")
@Feature("Authentication & Resources")
public class ReqresAuthTests extends BaseTest {

    @Test(description = "Verify successful registration with valid credentials")
    @Story("User Registration")
    @Severity(SeverityLevel.BLOCKER)
    public void testRegisterSuccessful() {
        ReqresAuthRequest req = ReqresAuthRequest.builder()
                .email("eve.holt@reqres.in")
                .password("pistol")
                .build();

        Response response = reqresClient.register(req);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ReqresAuthResponse authResp = response.as(ReqresAuthResponse.class);
        assertThat(authResp.getId()).isPositive();
        assertThat(authResp.getToken()).isNotBlank();
    }

    @Test(dataProvider = "invalidRegistrationData", dataProviderClass = ReqresDataProviders.class,
            description = "Verify registration fails with 400 Bad Request when required fields are missing")
    @Story("User Registration - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testRegisterUnsuccessful(String email, String password, String expectedError) {
        ReqresAuthRequest req = ReqresAuthRequest.builder()
                .email(email)
                .password(password)
                .build();

        Response response = reqresClient.register(req);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.BAD_REQUEST);

        ReqresAuthResponse authResp = response.as(ReqresAuthResponse.class);
        assertThat(authResp.getError()).isEqualTo(expectedError);
    }

    @Test(description = "Verify successful login with valid credentials")
    @Story("User Login")
    @Severity(SeverityLevel.BLOCKER)
    public void testLoginSuccessful() {
        ReqresAuthRequest req = ReqresAuthRequest.builder()
                .email("eve.holt@reqres.in")
                .password("cityslicka")
                .build();

        Response response = reqresClient.login(req);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ReqresAuthResponse authResp = response.as(ReqresAuthResponse.class);
        assertThat(authResp.getToken()).isNotBlank();
    }

    @Test(dataProvider = "invalidLoginData", dataProviderClass = ReqresDataProviders.class,
            description = "Verify login fails with 400 Bad Request on invalid credentials")
    @Story("User Login - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testLoginUnsuccessful(String email, String password, String expectedError) {
        ReqresAuthRequest req = ReqresAuthRequest.builder()
                .email(email)
                .password(password)
                .build();

        Response response = reqresClient.login(req);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.BAD_REQUEST);

        ReqresAuthResponse authResp = response.as(ReqresAuthResponse.class);
        assertThat(authResp.getError()).isEqualTo(expectedError);
    }

    @Test(description = "Verify retrieving unknown resources list")
    @Story("Resource Listing")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUnknownResourcesList() {
        Response response = reqresClient.getUnknownResources();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        assertThat(response.jsonPath().getList("data")).isNotEmpty();
    }

    @Test(description = "Verify retrieving single unknown resource by ID")
    @Story("Resource Details")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUnknownResourceById() {
        Response response = reqresClient.getUnknownResourceById(2);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        assertThat(response.jsonPath().getString("data.name")).isEqualTo("fuchsia rose");
    }

    @Test(description = "Verify 404 Not Found for non-existent unknown resource ID")
    @Story("Resource Details - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUnknownResourceNotFound() {
        Response response = reqresClient.getUnknownResourceById(23);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }
}

