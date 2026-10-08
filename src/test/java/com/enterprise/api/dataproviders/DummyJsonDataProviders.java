package com.enterprise.api.dataproviders;

import org.testng.annotations.DataProvider;

public final class DummyJsonDataProviders {

    private DummyJsonDataProviders() {
        // Prevent instantiation
    }

    @DataProvider(name = "productSearchQueries")
    public static Object[][] productSearchQueries() {
        return new Object[][]{
                {"phone", true},
                {"laptop", true},
                {"fragrance", true},
                {"watch", true},
                {"xyzNonExistentProductQuery123456", false}
        };
    }

    @DataProvider(name = "productCategories")
    public static Object[][] productCategories() {
        return new Object[][]{
                {"beauty"},
                {"fragrances"},
                {"furniture"},
                {"groceries"}
        };
    }

    @DataProvider(name = "paginationLimitsAndSkips")
    public static Object[][] paginationLimitsAndSkips() {
        return new Object[][]{
                {5, 0},
                {10, 10},
                {20, 40},
                {0, 0},
                {50, 0}
        };
    }

    @DataProvider(name = "userFilterCriteria")
    public static Object[][] userFilterCriteria() {
        return new Object[][]{
                {"gender", "female"},
                {"gender", "male"},
                {"bloodGroup", "A+"},
                {"bloodGroup", "O-"}
        };
    }

    @DataProvider(name = "invalidLoginCredentials")
    public static Object[][] invalidLoginCredentials() {
        return new Object[][]{
                {"invalidUserXYZ", "emilyspass", 400},
                {"emilys", "wrongPasswordXYZ", 400},
                {"", "emilyspass", 400},
                {"emilys", "", 400},
                {"", "", 400}
        };
    }
}

