package com.enterprise.api.dataproviders;

import org.testng.annotations.DataProvider;

public final class HttpBinDataProviders {

    private HttpBinDataProviders() {
        // Prevent instantiation
    }

    @DataProvider(name = "standardHttpStatusCodes")
    public static Object[][] standardHttpStatusCodes() {
        return new Object[][]{
                {200}, {201}, {202}, {204},
                {400}, {401}, {403}, {404}, {405}, {415}, {422},
                {500}, {502}, {503}
        };
    }

    @DataProvider(name = "customHeadersData")
    public static Object[][] customHeadersData() {
        return new Object[][]{
                {"X-Correlation-Id", "corr-uuid-12345"},
                {"X-Client-Version", "v2.5.0-enterprise"},
                {"X-Device-Type", "Web-Automation-TestNG"},
                {"Accept-Language", "en-US,en;q=0.9"}
        };
    }
}

