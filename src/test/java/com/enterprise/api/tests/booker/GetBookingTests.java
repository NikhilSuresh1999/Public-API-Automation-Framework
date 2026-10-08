package com.enterprise.api.tests.booker;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.BookerDataProviders;
import com.enterprise.api.models.booker.BookingIdResponse;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.models.booker.BookingResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker Microservice")
@Feature("Booking Retrieval & Queries")
public class GetBookingTests extends BaseTest {

    @Test(description = "Verify retrieving all booking IDs returns non-empty list")
    @Story("Query Booking IDs")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetAllBookingIds() {
        Response response = bookingClient.getAllBookingIds();

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);

        List<BookingIdResponse> ids = response.jsonPath().getList("", BookingIdResponse.class);
        assertThat(ids).as("Booking list should not be empty").isNotEmpty();
        assertThat(ids.get(0).getBookingid()).isPositive();
    }

    @Test(dataProvider = "searchFilterQueries", dataProviderClass = BookerDataProviders.class,
            description = "Verify filtering booking IDs with query parameters")
    @Story("Query Booking IDs - Filtering")
    @Severity(SeverityLevel.CRITICAL)
    public void testFilterBookingIds(String paramKey, String paramValue) {
        Response response = bookingClient.getBookingIdsWithFilter(Map.of(paramKey, paramValue));

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        List<?> list = response.jsonPath().getList("");
        assertThat(list).isNotNull();
    }

    @Test(description = "Verify retrieving single booking details by valid ID")
    @Story("Get Booking Details")
    @Severity(SeverityLevel.BLOCKER)
    public void testGetBookingDetailsById() {
        // Create a booking first to guarantee an existing ID
        BookingRequest request = DataGenerator.getRandomBooking();
        Response createResp = bookingClient.createBooking(request);
        Integer bookingId = createResp.jsonPath().getInt("bookingid");

        Response getResp = bookingClient.getBookingById(bookingId);

        AssertionUtils.assertStatusCode(getResp, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(getResp);
        AssertionUtils.assertJsonSchema(getResp, "schemas/booking-schema.json");

        BookingRequest retrieved = getResp.as(BookingRequest.class);
        assertThat(retrieved.getFirstname()).isEqualTo(request.getFirstname());
        assertThat(retrieved.getLastname()).isEqualTo(request.getLastname());
        assertThat(retrieved.getTotalprice()).isEqualTo(request.getTotalprice());
    }

    @Test(description = "Verify 404 Not Found when querying non-existent booking ID")
    @Story("Get Booking Details - Negative")
    @Severity(SeverityLevel.CRITICAL)
    public void testGetBookingByNonExistentId() {
        Response response = bookingClient.getBookingById(999999999);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify querying booking with negative ID returns 404")
    @Story("Get Booking Details - Boundary")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBookingByNegativeId() {
        Response response = bookingClient.getBookingById(-5);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify querying booking with string/alphanumeric ID returns 404")
    @Story("Get Booking Details - Negative")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBookingByInvalidFormatId() {
        Response response = bookingClient.getBookingById("invalid-id-string");
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify content negotiation when requesting XML accept header")
    @Story("Content Negotiation")
    @Severity(SeverityLevel.NORMAL)
    public void testGetBookingWithXmlAcceptHeader() {
        // Create booking first
        BookingRequest request = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(request).jsonPath().getInt("bookingid");

        Response response = bookingClient.getBookingByIdWithAcceptHeader(bookingId, "application/xml");
        // Restful-booker returns 200 with XML or 418
        assertThat(response.getStatusCode()).isIn(HttpStatusCodes.OK, 418);
    }
}

