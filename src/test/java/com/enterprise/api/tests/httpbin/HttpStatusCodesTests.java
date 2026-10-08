package com.enterprise.api.tests.httpbin;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.dataproviders.HttpBinDataProviders;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

@Epic("HttpBin Utility Service")
@Feature("HTTP Protocol & Status Codes")
public class HttpStatusCodesTests extends BaseTest {

    @Test(dataProvider = "standardHttpStatusCodes", dataProviderClass = HttpBinDataProviders.class,
            description = "Verify framework handles all standard 2xx, 4xx, and 5xx HTTP response codes accurately")
    @Story("Status Code Validation")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpStatusCodes(int expectedStatusCode) {
        Response response = httpBinClient.getStatus(expectedStatusCode);
        AssertionUtils.assertStatusCode(response, expectedStatusCode);
    }
}

