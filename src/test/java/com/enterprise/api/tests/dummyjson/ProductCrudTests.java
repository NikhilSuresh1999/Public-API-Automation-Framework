package com.enterprise.api.tests.dummyjson;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.DummyJsonDataProviders;
import com.enterprise.api.models.dummyjson.ProductListResponse;
import com.enterprise.api.models.dummyjson.ProductRequest;
import com.enterprise.api.models.dummyjson.ProductResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("DummyJSON Microservice")
@Feature("Product Catalog Management")
public class ProductCrudTests extends BaseTest {

    @Test(description = "Verify retrieving all products returns populated list and default pagination")
    @Story("Product Listing")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetAllProducts() {
        Response response = productClient.getAllProducts();

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        ProductListResponse list = response.as(ProductListResponse.class);
        assertThat(list.getProducts()).isNotEmpty();
        assertThat(list.getTotal()).isGreaterThan(0);
        assertThat(list.getLimit()).isEqualTo(30);
    }

    @Test(dataProvider = "paginationLimitsAndSkips", dataProviderClass = DummyJsonDataProviders.class,
            description = "Verify pagination with variable limit and skip parameters")
    @Story("Product Pagination")
    @Severity(SeverityLevel.CRITICAL)
    public void testProductPagination(int limit, int skip) {
        Response response = productClient.getProductsWithParams(Map.of("limit", limit, "skip", skip));

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        ProductListResponse list = response.as(ProductListResponse.class);

        if (limit > 0) {
            assertThat(list.getProducts()).hasSizeLessThanOrEqualTo(limit);
        } else {
            // DummyJSON treats limit=0 as returning all records without pagination
            assertThat(list.getProducts()).isNotEmpty();
        }
        assertThat(list.getSkip()).isEqualTo(skip);
    }

    @Test(description = "Verify retrieving single product by valid ID")
    @Story("Product Details")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetProductById() {
        Response response = productClient.getProductById(1);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
        AssertionUtils.assertJsonSchema(response, "schemas/product-schema.json");

        ProductResponse product = response.as(ProductResponse.class);
        assertThat(product.getId()).isEqualTo(1);
        assertThat(product.getTitle()).isNotBlank();
        assertThat(product.getPrice()).isPositive();
    }

    @Test(description = "Verify 404 Not Found when requesting non-existent product ID")
    @Story("Product Details - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetProductByNonExistentId() {
        Response response = productClient.getProductById(999999);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
        AssertionUtils.assertBodyContains(response, "not found");
    }

    @Test(dataProvider = "productSearchQueries", dataProviderClass = DummyJsonDataProviders.class,
            description = "Verify product search functionality")
    @Story("Product Search")
    @Severity(SeverityLevel.CRITICAL)
    public void testSearchProducts(String query, boolean shouldHaveResults) {
        Response response = productClient.searchProducts(query);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        ProductListResponse list = response.as(ProductListResponse.class);

        if (shouldHaveResults) {
            assertThat(list.getProducts()).as("Search for '" + query + "' should return results").isNotEmpty();
        } else {
            assertThat(list.getProducts()).as("Search for '" + query + "' should return empty list").isEmpty();
        }
    }

    @Test(description = "Verify retrieving all categories")
    @Story("Product Categories")
    @Severity(SeverityLevel.NORMAL)
    public void testGetProductCategories() {
        Response response = productClient.getCategories();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        List<?> categories = response.jsonPath().getList("");
        assertThat(categories).isNotEmpty();
    }

    @Test(dataProvider = "productCategories", dataProviderClass = DummyJsonDataProviders.class,
            description = "Verify filtering products by category")
    @Story("Product Filtering")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetProductsByCategory(String category) {
        Response response = productClient.getProductsByCategory(category);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        ProductListResponse list = response.as(ProductListResponse.class);
        assertThat(list.getProducts()).isNotEmpty();
        list.getProducts().forEach(p -> assertThat(p.getCategory()).isEqualToIgnoringCase(category));
    }

    @Test(description = "Verify creating a new product")
    @Story("Create Product")
    @Severity(SeverityLevel.BLOCKER)
    public void testAddProduct() {
        ProductRequest newProduct = DataGenerator.getRandomProduct();

        Response response = productClient.addProduct(newProduct);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.CREATED);

        ProductResponse created = response.as(ProductResponse.class);
        assertThat(created.getId()).isNotNull().isPositive();
        assertThat(created.getTitle()).isEqualTo(newProduct.getTitle());
        assertThat(created.getPrice()).isEqualTo(newProduct.getPrice());
    }

    @Test(description = "Verify full update of an existing product (PUT)")
    @Story("Update Product")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateProduct() {
        ProductRequest updateData = DataGenerator.getRandomProduct();
        updateData.setTitle("Completely Updated Title");

        Response response = productClient.updateProduct(1, updateData);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ProductResponse updated = response.as(ProductResponse.class);
        assertThat(updated.getTitle()).isEqualTo("Completely Updated Title");
    }

    @Test(description = "Verify partial update of an existing product (PATCH)")
    @Story("Partial Update Product")
    @Severity(SeverityLevel.CRITICAL)
    public void testPatchProduct() {
        Map<String, Object> partialFields = Map.of(
                "title", "Patched Product Title",
                "price", 99.99
        );

        Response response = productClient.patchProduct(1, partialFields);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ProductResponse patched = response.as(ProductResponse.class);
        assertThat(patched.getTitle()).isEqualTo("Patched Product Title");
        assertThat(patched.getPrice()).isEqualTo(99.99);
    }

    @Test(description = "Verify deletion of a product (DELETE)")
    @Story("Delete Product")
    @Severity(SeverityLevel.BLOCKER)
    public void testDeleteProduct() {
        Response response = productClient.deleteProduct(1);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        ProductResponse deleted = response.as(ProductResponse.class);
        assertThat(deleted.getIsDeleted()).isTrue();
        assertThat(deleted.getDeletedOn()).isNotBlank();
    }

    @Test(description = "Verify sorting products by price in ascending order")
    @Story("Product Sorting")
    @Severity(SeverityLevel.NORMAL)
    public void testSortProductsByPriceAscending() {
        Response response = productClient.getProductsWithParams(Map.of("sortBy", "price", "order", "asc"));
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        List<Double> prices = response.jsonPath().getList("products.price", Double.class);
        assertThat(prices).isNotEmpty();
        for (int i = 0; i < prices.size() - 1; i++) {
            assertThat(prices.get(i)).isLessThanOrEqualTo(prices.get(i + 1));
        }
    }
}

