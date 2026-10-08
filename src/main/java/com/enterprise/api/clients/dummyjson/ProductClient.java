package com.enterprise.api.clients.dummyjson;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.dummyjson.ProductRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class ProductClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getDummyJsonSpec();

    @Step("Get all products with default pagination")
    public Response getAllProducts() {
        return given().spec(spec)
                .when()
                .get(EndPoints.DUMMY_PRODUCTS);
    }

    @Step("Get products with query parameters: {0}")
    public Response getProductsWithParams(Map<String, ?> params) {
        return given().spec(spec)
                .queryParams(params)
                .when()
                .get(EndPoints.DUMMY_PRODUCTS);
    }

    @Step("Get product by ID: {0}")
    public Response getProductById(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .get(EndPoints.DUMMY_PRODUCT_BY_ID);
    }

    @Step("Search products with query: {0}")
    public Response searchProducts(String query) {
        return given().spec(spec)
                .queryParam("q", query)
                .when()
                .get(EndPoints.DUMMY_PRODUCT_SEARCH);
    }

    @Step("Get all product categories")
    public Response getCategories() {
        return given().spec(spec)
                .when()
                .get(EndPoints.DUMMY_PRODUCT_CATEGORIES);
    }

    @Step("Get products by category: {0}")
    public Response getProductsByCategory(String category) {
        return given().spec(spec)
                .pathParam("category", category)
                .when()
                .get(EndPoints.DUMMY_PRODUCT_BY_CATEGORY);
    }

    @Step("Add a new product")
    public Response addProduct(ProductRequest product) {
        return given().spec(spec)
                .body(product)
                .when()
                .post(EndPoints.DUMMY_PRODUCT_ADD);
    }

    @Step("Update product ID: {0}")
    public Response updateProduct(Object id, ProductRequest product) {
        return given().spec(spec)
                .pathParam("id", id)
                .body(product)
                .when()
                .put(EndPoints.DUMMY_PRODUCT_BY_ID);
    }

    @Step("Partial update (PATCH) product ID: {0}")
    public Response patchProduct(Object id, Map<String, Object> partialFields) {
        return given().spec(spec)
                .pathParam("id", id)
                .body(partialFields)
                .when()
                .patch(EndPoints.DUMMY_PRODUCT_BY_ID);
    }

    @Step("Delete product ID: {0}")
    public Response deleteProduct(Object id) {
        return given().spec(spec)
                .pathParam("id", id)
                .when()
                .delete(EndPoints.DUMMY_PRODUCT_BY_ID);
    }
}

