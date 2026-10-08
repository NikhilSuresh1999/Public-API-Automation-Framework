package com.enterprise.api.dataproviders;

import org.testng.annotations.DataProvider;

public final class ReqresDataProviders {

    private ReqresDataProviders() {
        // Prevent instantiation
    }

    @DataProvider(name = "userDataProvider")
    public static Object[][] userDataProvider() {
        return new Object[][]{
                {"Alice Johnson", "QA Architect"},
                {"Bob Smith", "DevOps Engineer"},
                {"Charlie Davis", "SDET Lead"},
                {"Diana Prince", "Product Manager"},
                {"Evan Wright", "Security Analyst"}
        };
    }

    @DataProvider(name = "userPaginationPages")
    public static Object[][] userPaginationPages() {
        return new Object[][]{
                {1, 6},
                {2, 6}
        };
    }

    @DataProvider(name = "invalidRegistrationData")
    public static Object[][] invalidRegistrationData() {
        return new Object[][]{
                {"sydney@fife", null, "Missing password"},
                {"sydney@fife", "", "Missing password"},
                {null, "pistol", "Missing email or username"},
                {"", "pistol", "Missing email or username"}
        };
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {
        return new Object[][]{
                {"peter@klaven", null, "Missing password"},
                {null, "cityslicka", "Missing email or username"},
                {"nonexistent@reqres.in", "cityslicka", "user not found"}
        };
    }
}

