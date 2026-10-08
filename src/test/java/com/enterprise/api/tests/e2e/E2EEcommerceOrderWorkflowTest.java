package com.enterprise.api.tests.e2e;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.dummyjson.*;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("End-to-End Business Scenarios")
@Feature("E-Commerce Order & Shopping Journey")
public class E2EEcommerceOrderWorkflowTest extends BaseTest {

    @Test(description = "E2E Shopping Flow: Authenticate -> Discover Products -> Inspect Profile -> Create Cart -> Verify Checkout")
    @Story("Complete E-Commerce Flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Simulates complete consumer journey: Logging in, browsing catalog, constructing cart with items, and validating calculations.")
    public void testCompleteEcommerceWorkflow() {
        // Step 1: User logs in and acquires auth token
        Allure.step("Step 1: User logs in and acquires token");
        Response loginResp = dummyAuthClient.login(
                ConfigurationManager.get().dummyJsonUsername(),
                ConfigurationManager.get().dummyJsonPassword()
        );
        AssertionUtils.assertStatusCode(loginResp, HttpStatusCodes.OK);
        LoginResponse loginData = loginResp.as(LoginResponse.class);
        String token = loginData.getAccessToken();
        Integer userId = loginData.getId();
        assertThat(token).isNotBlank();
        assertThat(userId).isPositive();

        // Step 2: Fetch and verify authenticated profile
        Allure.step("Step 2: Fetch user profile");
        Response profileResp = dummyAuthClient.getCurrentUser(token);
        AssertionUtils.assertStatusCode(profileResp, HttpStatusCodes.OK);
        UserResponse userProfile = profileResp.as(UserResponse.class);
        assertThat(userProfile.getId()).isEqualTo(userId);

        // Step 3: Browse product catalog for items to purchase
        Allure.step("Step 3: Browse product catalog");
        Response productsResp = productClient.getAllProducts();
        AssertionUtils.assertStatusCode(productsResp, HttpStatusCodes.OK);
        ProductListResponse catalog = productsResp.as(ProductListResponse.class);
        assertThat(catalog.getProducts()).hasSizeGreaterThanOrEqualTo(2);

        ProductResponse firstProduct = catalog.getProducts().get(0);
        ProductResponse secondProduct = catalog.getProducts().get(1);

        // Step 4: Add selected items to cart
        Allure.step("Step 4: Add selected products to cart");
        CartRequest cartRequest = CartRequest.builder()
                .userId(userId)
                .products(List.of(
                        CartRequest.CartItemInput.builder().id(firstProduct.getId()).quantity(2).build(),
                        CartRequest.CartItemInput.builder().id(secondProduct.getId()).quantity(1).build()
                ))
                .build();

        Response addCartResp = cartClient.addCart(cartRequest);
        AssertionUtils.assertStatusCode(addCartResp, HttpStatusCodes.CREATED);
        CartResponse createdCart = addCartResp.as(CartResponse.class);

        // Step 5: Verify cart calculations
        Allure.step("Step 5: Verify calculations of created cart");
        assertThat(createdCart.getUserId()).isEqualTo(userId);
        assertThat(createdCart.getProducts()).hasSize(2);
        assertThat(createdCart.getTotalQuantity()).isEqualTo(3);
        assertThat(createdCart.getTotal()).isGreaterThan(0.0);

        // Step 6: Cleanup / Delete cart (using simulated cart 1)
        Allure.step("Step 6: Cleanup / Delete cart");
        Response deleteResp = cartClient.deleteCart(1);
        AssertionUtils.assertStatusCode(deleteResp, HttpStatusCodes.OK);
        CartResponse deleted = deleteResp.as(CartResponse.class);
        assertThat(deleted.getIsDeleted()).isTrue();
    }
}

