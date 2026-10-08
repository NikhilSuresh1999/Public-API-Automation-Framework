package com.enterprise.api.tests.dummyjson;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.dummyjson.CartListResponse;
import com.enterprise.api.models.dummyjson.CartProduct;
import com.enterprise.api.models.dummyjson.CartRequest;
import com.enterprise.api.models.dummyjson.CartResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("DummyJSON Microservice")
@Feature("Shopping Cart & Checkout")
public class CartManagementTests extends BaseTest {

    @Test(description = "Verify retrieving all carts returns populated list")
    @Story("Cart Listing")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetAllCarts() {
        Response response = cartClient.getAllCarts();

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        CartListResponse list = response.as(CartListResponse.class);
        assertThat(list.getCarts()).isNotEmpty();
        assertThat(list.getTotal()).isGreaterThan(0);
    }

    @Test(description = "Verify retrieving single cart by valid ID and validating schema")
    @Story("Cart Details & Schema")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetCartById() {
        Response response = cartClient.getCartById(1);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
        AssertionUtils.assertJsonSchema(response, "schemas/cart-schema.json");

        CartResponse cart = response.as(CartResponse.class);
        assertThat(cart.getId()).isEqualTo(1);
        assertThat(cart.getProducts()).isNotEmpty();
        assertThat(cart.getUserId()).isPositive();
    }

    @Test(description = "Verify cart total math and item quantity calculations")
    @Story("Cart Mathematical Verification")
    @Severity(SeverityLevel.CRITICAL)
    public void testCartCalculationsIntegrity() {
        Response response = cartClient.getCartById(1);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        CartResponse cart = response.as(CartResponse.class);

        int calculatedTotalQuantity = cart.getProducts().stream()
                .mapToInt(CartProduct::getQuantity)
                .sum();

        assertThat(cart.getTotalQuantity())
                .as("Verify totalQuantity equals sum of item quantities")
                .isEqualTo(calculatedTotalQuantity);

        assertThat(cart.getTotalProducts())
                .as("Verify totalProducts equals size of product list")
                .isEqualTo(cart.getProducts().size());
    }

    @Test(description = "Verify retrieving carts for a specific user ID")
    @Story("User Carts")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetCartsByUserId() {
        Response response = cartClient.getCartsByUserId(5);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        CartListResponse userCarts = response.as(CartListResponse.class);
        assertThat(userCarts.getCarts()).isNotNull();
    }

    @Test(description = "Verify adding products to cart (POST /carts/add)")
    @Story("Add to Cart")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddProductsToCart() {
        CartRequest cartRequest = DataGenerator.getRandomCart(1);

        Response response = cartClient.addCart(cartRequest);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        CartResponse createdCart = response.as(CartResponse.class);
        assertThat(createdCart.getId()).isNotNull();
        assertThat(createdCart.getUserId()).isEqualTo(1);
        assertThat(createdCart.getProducts()).isNotEmpty();
        assertThat(createdCart.getTotal()).isGreaterThan(0);
    }

    @Test(description = "Verify deleting a cart (DELETE /carts/{id})")
    @Story("Delete Cart")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteCart() {
        Response response = cartClient.deleteCart(1);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        CartResponse deleted = response.as(CartResponse.class);
        assertThat(deleted.getIsDeleted()).isTrue();
        assertThat(deleted.getDeletedOn()).isNotBlank();
    }

    @Test(description = "Verify 404 Not Found when retrieving non-existent cart ID")
    @Story("Cart Details - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetCartByNonExistentId() {
        Response response = cartClient.getCartById(999999);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }
}

