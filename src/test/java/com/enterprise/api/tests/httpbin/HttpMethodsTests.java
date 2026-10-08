package com.enterprise.api.tests.httpbin;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.httpbin.HttpBinResponse;
import com.enterprise.api.utils.AssertionUtils;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("HttpBin Utility Service")
@Feature("HTTP Verbs & Payload Echoing")
public class HttpMethodsTests extends BaseTest {

    @Test(description = "Verify GET method echoes query parameters properly")
    @Story("HTTP GET")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpGetWithQueryParams() {
        Response response = httpBinClient.callGet(Map.of("category", "electronics", "page", 2));

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        HttpBinResponse binResp = response.as(HttpBinResponse.class);

        assertThat(binResp.getArgs()).containsEntry("category", "electronics");
        assertThat(binResp.getArgs()).containsEntry("page", "2");
    }

    @Test(description = "Verify POST method echoes JSON request payload properly")
    @Story("HTTP POST")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpPostWithJsonPayload() {
        Map<String, Object> payload = Map.of("title", "Enterprise Automation", "status", "ACTIVE");

        Response response = httpBinClient.callPostJson(payload);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        HttpBinResponse binResp = response.as(HttpBinResponse.class);
        assertThat(binResp.getData()).contains("Enterprise Automation");
    }

    @Test(description = "Verify PUT method echoes updated payload properly")
    @Story("HTTP PUT")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpPutWithJsonPayload() {
        Map<String, Object> payload = Map.of("id", 101, "role", "ADMIN");

        Response response = httpBinClient.callPutJson(payload);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        HttpBinResponse binResp = response.as(HttpBinResponse.class);
        assertThat(binResp.getData()).contains("ADMIN");
    }

    @Test(description = "Verify PATCH method echoes modified partial payload properly")
    @Story("HTTP PATCH")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpPatchWithJsonPayload() {
        Map<String, Object> payload = Map.of("updatedFlag", true);

        Response response = httpBinClient.callPatchJson(payload);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        HttpBinResponse binResp = response.as(HttpBinResponse.class);
        assertThat(binResp.getData()).contains("updatedFlag");
    }

    @Test(description = "Verify DELETE method returns successful response")
    @Story("HTTP DELETE")
    @Severity(SeverityLevel.CRITICAL)
    public void testHttpDeleteMethod() {
        Response response = httpBinClient.callDelete();
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
    }
}

