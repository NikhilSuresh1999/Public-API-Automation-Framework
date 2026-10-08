package com.enterprise.api.tests.dummyjson;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.dummyjson.ProductListResponse;
import com.enterprise.api.models.dummyjson.ProductRequest;
import com.enterprise.api.models.dummyjson.ProductResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("DummyJSON Microservice")
@Feature("Product Data Validation & Integrity")
public class ProductValidationTests extends BaseTest {

    @Test(description = "Verify that all returned products have rating within valid scale (0.0 to 5.0)")
    @Story("Rating Range Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testProductRatingRange() {
        Response response = productClient.getAllProducts();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ProductListResponse list = response.as(ProductListResponse.class);
        list.getProducts().forEach(p -> {
            if (p.getRating() != null) {
                assertThat(p.getRating())
                        .as("Product ID " + p.getId() + " rating should be between 0 and 5")
                        .isBetween(0.0, 5.0);
            }
        });
    }

    @Test(description = "Verify that all returned products have non-negative stock")
    @Story("Stock Quantity Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testProductStockNonNegative() {
        Response response = productClient.getAllProducts();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ProductListResponse list = response.as(ProductListResponse.class);
        list.getProducts().forEach(p -> {
            if (p.getStock() != null) {
                assertThat(p.getStock())
                        .as("Product ID " + p.getId() + " stock should be >= 0")
                        .isGreaterThanOrEqualTo(0);
            }
        });
    }

    @Test(description = "Verify adding a product with boundary zero price")
    @Story("Boundary Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testAddProductZeroPrice() {
        ProductRequest product = ProductRequest.builder()
                .title("Free Promotional Sample")
                .price(0.0)
                .stock(10)
                .category("groceries")
                .build();

        Response response = productClient.addProduct(product);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        ProductResponse created = response.as(ProductResponse.class);
        assertThat(created.getPrice()).isEqualTo(0.0);
    }

    @Test(description = "Verify adding a product with huge price value")
    @Story("Boundary Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testAddProductHighPrice() {
        ProductRequest product = ProductRequest.builder()
                .title("Luxury Yacht")
                .price(9999999.99)
                .stock(1)
                .category("luxury")
                .build();

        Response response = productClient.addProduct(product);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        ProductResponse created = response.as(ProductResponse.class);
        assertThat(created.getPrice()).isEqualTo(9999999.99);
    }

    @Test(description = "Verify adding a product with extreme string length for description")
    @Story("Boundary Validation - Payload Size")
    @Severity(SeverityLevel.NORMAL)
    public void testAddProductLargeDescription() {
        String largeDescription = "A".repeat(4000);
        ProductRequest product = ProductRequest.builder()
                .title("Large Description Product")
                .description(largeDescription)
                .price(19.99)
                .category("groceries")
                .build();

        Response response = productClient.addProduct(product);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        ProductResponse created = response.as(ProductResponse.class);
        assertThat(created.getDescription()).isEqualTo(largeDescription);
    }
}

