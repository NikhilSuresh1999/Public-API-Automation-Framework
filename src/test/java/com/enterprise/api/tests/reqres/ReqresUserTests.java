package com.enterprise.api.tests.reqres;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.ReqresDataProviders;
import com.enterprise.api.models.reqres.ReqresListResponse;
import com.enterprise.api.models.reqres.ReqresUserRequest;
import com.enterprise.api.models.reqres.ReqresUserResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("ReqRes Mock Microservice")
@Feature("User Resource Lifecycle")
public class ReqresUserTests extends BaseTest {

    @Test(dataProvider = "userPaginationPages", dataProviderClass = ReqresDataProviders.class,
            description = "Verify user pagination returns correct page and record counts")
    @Story("User Pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUsersPagination(int page, int expectedPerPage) {
        Response response = reqresClient.getUsers(page);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        ReqresListResponse list = response.as(ReqresListResponse.class);
        assertThat(list.getPage()).isEqualTo(page);
        assertThat(list.getPer_page()).isEqualTo(expectedPerPage);
        assertThat(list.getData()).isNotEmpty();
    }

    @Test(description = "Verify retrieving single user by ID and validating contract schema")
    @Story("User Details & Contract")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetSingleUserById() {
        Response response = reqresClient.getUserById(2);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
        AssertionUtils.assertJsonSchema(response, "schemas/reqres-user-schema.json");

        ReqresUserResponse user = response.as(ReqresUserResponse.class);
        assertThat(user.getData().getId()).isEqualTo(2);
        assertThat(user.getData().getEmail()).isEqualTo("janet.weaver@reqres.in");
        assertThat(user.getData().getFirst_name()).isEqualTo("Janet");
    }

    @Test(description = "Verify 404 Not Found when retrieving non-existent user ID")
    @Story("User Details - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserNotFound() {
        Response response = reqresClient.getUserById(23);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(dataProvider = "userDataProvider", dataProviderClass = ReqresDataProviders.class,
            description = "Verify creating users with data-driven profiles")
    @Story("Create User")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateUser(String name, String job) {
        ReqresUserRequest req = ReqresUserRequest.builder().name(name).job(job).build();

        Response response = reqresClient.createUser(req);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        ReqresUserResponse created = response.as(ReqresUserResponse.class);
        assertThat(created.getName()).isEqualTo(name);
        assertThat(created.getJob()).isEqualTo(job);
        assertThat(created.getId()).isNotBlank();
        assertThat(created.getCreatedAt()).isNotBlank();
    }

    @Test(description = "Verify full update of a user using PUT")
    @Story("Update User (PUT)")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserPut() {
        ReqresUserRequest updateReq = ReqresUserRequest.builder()
                .name("Neo")
                .job("The One")
                .build();

        Response response = reqresClient.updateUserPut(2, updateReq);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ReqresUserResponse updated = response.as(ReqresUserResponse.class);
        assertThat(updated.getName()).isEqualTo("Neo");
        assertThat(updated.getJob()).isEqualTo("The One");
        assertThat(updated.getUpdatedAt()).isNotBlank();
    }

    @Test(description = "Verify partial update of a user using PATCH")
    @Story("Update User (PATCH)")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateUserPatch() {
        ReqresUserRequest patchReq = ReqresUserRequest.builder()
                .job("Lead Matrix Architect")
                .build();

        Response response = reqresClient.updateUserPatch(2, patchReq);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ReqresUserResponse updated = response.as(ReqresUserResponse.class);
        assertThat(updated.getJob()).isEqualTo("Lead Matrix Architect");
        assertThat(updated.getUpdatedAt()).isNotBlank();
    }

    @Test(description = "Verify deleting a user returns 204 No Content")
    @Story("Delete User")
    @Severity(SeverityLevel.BLOCKER)
    public void testDeleteUser() {
        Response response = reqresClient.deleteUser(2);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NO_CONTENT);
    }

    @Test(description = "Verify API handling and timeout under simulated network latency (delay=2)")
    @Story("Performance & Latency Resilience")
    @Severity(SeverityLevel.NORMAL)
    public void testDelayedResponseHandling() {
        Response response = reqresClient.getUsersWithDelay(2);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        assertThat(response.getTime()).isGreaterThanOrEqualTo(1500L);
    }
}

