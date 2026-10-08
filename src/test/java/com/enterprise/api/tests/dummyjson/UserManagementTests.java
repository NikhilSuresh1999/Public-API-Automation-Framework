package com.enterprise.api.tests.dummyjson;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.DummyJsonDataProviders;
import com.enterprise.api.models.dummyjson.UserListResponse;
import com.enterprise.api.models.dummyjson.UserResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("DummyJSON Microservice")
@Feature("User Profile & Directory Management")
public class UserManagementTests extends BaseTest {

    @Test(description = "Verify retrieving all users returns non-empty list and valid pagination")
    @Story("User Directory")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetAllUsers() {
        Response response = userClient.getAllUsers();

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        UserListResponse list = response.as(UserListResponse.class);
        assertThat(list.getUsers()).isNotEmpty();
        assertThat(list.getTotal()).isGreaterThan(0);
        assertThat(list.getLimit()).isEqualTo(30);
    }

    @Test(description = "Verify retrieving single user details by ID")
    @Story("User Details")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetUserById() {
        Response response = userClient.getUserById(1);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
        AssertionUtils.assertJsonSchema(response, "schemas/user-schema.json");

        UserResponse user = response.as(UserResponse.class);
        assertThat(user.getId()).isEqualTo(1);
        assertThat(user.getFirstName()).isNotBlank();
        assertThat(user.getLastName()).isNotBlank();
        assertThat(user.getEmail()).contains("@");
        assertThat(user.getAddress()).isNotEmpty();
    }

    @Test(description = "Verify 404 Not Found for non-existent user ID")
    @Story("User Details - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetUserByNonExistentId() {
        Response response = userClient.getUserById(999999);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify 404 Not Found for negative user ID")
    @Story("User Details - Boundary")
    @Severity(SeverityLevel.NORMAL)
    public void testGetUserByNegativeId() {
        Response response = userClient.getUserById(-1);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify searching users by keyword query")
    @Story("User Search")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchUsers() {
        Response response = userClient.searchUsers("Emily");

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        UserListResponse list = response.as(UserListResponse.class);
        assertThat(list.getUsers()).isNotEmpty();
        assertThat(list.getUsers().get(0).getFirstName()).containsIgnoringCase("Emily");
    }

    @Test(dataProvider = "userFilterCriteria", dataProviderClass = DummyJsonDataProviders.class,
            description = "Verify filtering users by specific attribute criteria")
    @Story("User Filtering")
    @Severity(SeverityLevel.CRITICAL)
    public void testFilterUsers(String filterKey, String filterValue) {
        Response response = userClient.filterUsers(filterKey, filterValue);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        UserListResponse list = response.as(UserListResponse.class);
        assertThat(list.getUsers()).isNotEmpty();

        if ("gender".equals(filterKey)) {
            list.getUsers().forEach(u -> assertThat(u.getGender()).isEqualToIgnoringCase(filterValue));
        } else if ("bloodGroup".equals(filterKey)) {
            list.getUsers().forEach(u -> assertThat(u.getBloodGroup()).isEqualTo(filterValue));
        }
    }

    @Test(description = "Verify user email format integrity across the dataset")
    @Story("User Data Integrity")
    @Severity(SeverityLevel.NORMAL)
    public void testUserEmailFormatIntegrity() {
        Response response = userClient.getUsersWithParams(Map.of("limit", 20));
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        UserListResponse list = response.as(UserListResponse.class);
        list.getUsers().forEach(user ->
                assertThat(user.getEmail())
                        .as("Verify valid email for user ID: " + user.getId())
                        .matches("^[A-Za-z0-9+_.-]+@(.+)$")
        );
    }
}

