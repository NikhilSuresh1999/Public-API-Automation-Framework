package com.enterprise.api.clients.dummyjson;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.dummyjson.CartRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class CartClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getDummyJsonSpec();

    @Step("Get all carts")
    public Response getAllCarts() {
        return given().spec(spec)
                .when()
                .get(EndPoints.DUMMY_CARTS);
    }

    @Step("Get cart by ID: {0}")
    public Response getCartById(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(EndPoints.DUMMY_CART_BY_ID);
    }

    @Step("Get carts by user ID: {0}")
    public Response getCartsByUserId(Object userId) {
        return given().spec(spec)
                .pathParam("userId", userId)
                .when()
                .get(EndPoints.DUMMY_CARTS_BY_USER);
    }

    @Step("Add items to cart")
    public Response addCart(CartRequest cartRequest) {
        return given().spec(spec)
                .body(cartRequest)
                .when()
                .post(EndPoints.DUMMY_CART_ADD);
    }

    @Step("Delete cart ID: {0}")
    public Response deleteCart(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .delete(EndPoints.DUMMY_CART_BY_ID);
    }
}

