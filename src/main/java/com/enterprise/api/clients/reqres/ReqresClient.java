package com.enterprise.api.clients.reqres;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.reqres.ReqresAuthRequest;
import com.enterprise.api.models.reqres.ReqresUserRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class ReqresClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getReqresSpec();

    @Step("Get users on page: {0}")
    public Response getUsers(int page) {
        return given().spec(spec)
                .queryParam("page", page)
                .when()
                .get(EndPoints.REQRES_USERS);
    }

    @Step("Get users with custom delay: {0} seconds")
    public Response getUsersWithDelay(int delaySeconds) {
        return given().spec(spec)
                .queryParam("delay", delaySeconds)
                .when()
                .get(EndPoints.REQRES_USERS);
    }

    @Step("Get single user by ID: {0}")
    public Response getUserById(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(EndPoints.REQRES_USER_BY_ID);
    }

    @Step("Create user with name: {0.name}, job: {0.job}")
    public Response createUser(ReqresUserRequest request) {
        return given().spec(spec)
                .body(request)
                .when()
                .post(EndPoints.REQRES_USERS);
    }

    @Step("Update user ID: {0} with PUT")
    public Response updateUserPut(Object id, ReqresUserRequest request) {
        return given().spec(spec)
                .pathParam("id", id)
                .body(request)
                .when()
                .put(EndPoints.REQRES_USER_BY_ID);
    }

    @Step("Update user ID: {0} with PATCH")
    public Response updateUserPatch(Object id, ReqresUserRequest request) {
        return given().spec(spec)
                .pathParam("id", id)
                .body(request)
                .when()
                .patch(EndPoints.REQRES_USER_BY_ID);
    }

    @Step("Delete user ID: {0}")
    public Response deleteUser(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .delete(EndPoints.REQRES_USER_BY_ID);
    }

    @Step("Register user")
    public Response register(ReqresAuthRequest request) {
        return given().spec(spec)
                .body(request)
                .when()
                .post(EndPoints.REQRES_REGISTER);
    }

    @Step("Login user")
    public Response login(ReqresAuthRequest request) {
        return given().spec(spec)
                .body(request)
                .when()
                .post(EndPoints.REQRES_LOGIN);
    }

    @Step("Get unknown resource list")
    public Response getUnknownResources() {
        return given().spec(spec)
                .when()
                .get(EndPoints.REQRES_UNKNOWN);
    }

    @Step("Get unknown resource by ID: {0}")
    public Response getUnknownResourceById(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(EndPoints.REQRES_UNKNOWN_BY_ID);
    }
}

