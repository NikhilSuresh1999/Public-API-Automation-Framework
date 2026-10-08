package com.enterprise.api.tests.booker;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.dataproviders.BookerDataProviders;
import com.enterprise.api.models.booker.BookingDates;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.models.booker.BookingResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker Microservice")
@Feature("Booking Management")
public class CreateBookingTests extends BaseTest {

    @Test(description = "Verify successful creation of a booking with dynamic random data")
    @Story("Create Booking")
    @Severity(SeverityLevel.BLOCKER)
    public void testCreateBookingWithRandomData() {
        BookingRequest request = DataGenerator.getRandomBooking();

        Response response = bookingClient.createBooking(request);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(response);
        AssertionUtils.assertJsonSchema(response, "schemas/booking-response-schema.json");

        BookingResponse created = response.as(BookingResponse.class);
        assertThat(created.getBookingid()).isNotNull().isPositive();
        assertThat(created.getBooking().getFirstname()).isEqualTo(request.getFirstname());
        assertThat(created.getBooking().getLastname()).isEqualTo(request.getLastname());
        assertThat(created.getBooking().getTotalprice()).isEqualTo(request.getTotalprice());
        assertThat(created.getBooking().getDepositpaid()).isEqualTo(request.getDepositpaid());
        assertThat(created.getBooking().getBookingdates().getCheckin()).isEqualTo(request.getBookingdates().getCheckin());
        assertThat(created.getBooking().getBookingdates().getCheckout()).isEqualTo(request.getBookingdates().getCheckout());
    }

    @Test(dataProvider = "validBookingPayloads", dataProviderClass = BookerDataProviders.class,
            description = "Verify booking creation across multiple persona scenarios")
    @Story("Create Booking - Data Driven")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateBookingDataDriven(BookingRequest bookingRequest) {
        Response response = bookingClient.createBooking(bookingRequest);

        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);
        BookingResponse created = response.as(BookingResponse.class);

        assertThat(created.getBookingid()).isPositive();
        assertThat(created.getBooking().getFirstname()).isEqualTo(bookingRequest.getFirstname());
        assertThat(created.getBooking().getTotalprice()).isEqualTo(bookingRequest.getTotalprice());
    }

    @Test(dataProvider = "invalidBookingPayloads", dataProviderClass = BookerDataProviders.class,
            description = "Verify API handling when required booking fields are missing")
    @Story("Create Booking - Negative Validation")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateBookingWithMissingFields(BookingRequest invalidRequest, String scenarioDescription) {
        Response response = bookingClient.createBooking(invalidRequest);

        // API should return 500/400 for missing mandatory booking fields
        assertThat(response.getStatusCode())
                .as("Verify rejection status for: " + scenarioDescription)
                .isIn(HttpStatusCodes.INTERNAL_SERVER_ERROR, HttpStatusCodes.BAD_REQUEST);
    }

    @Test(description = "Verify booking creation with boundary zero price")
    @Story("Create Booking - Boundary")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateBookingWithZeroPrice() {
        BookingRequest request = DataGenerator.getRandomBooking();
        request.setTotalprice(0);

        Response response = bookingClient.createBooking(request);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        BookingResponse created = response.as(BookingResponse.class);
        assertThat(created.getBooking().getTotalprice()).isEqualTo(0);
    }

    @Test(description = "Verify booking creation with special characters in names")
    @Story("Create Booking - Security & Sanitization")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateBookingWithSpecialCharacters() {
        BookingRequest request = DataGenerator.getRandomBooking();
        request.setFirstname("O'Connor-Smith & Co.");
        request.setLastname("Renée 123#!");

        Response response = bookingClient.createBooking(request);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        BookingResponse created = response.as(BookingResponse.class);
        assertThat(created.getBooking().getFirstname()).isEqualTo("O'Connor-Smith & Co.");
    }

    @Test(description = "Verify booking creation with HTML/Script tags (XSS Resilience)")
    @Story("Create Booking - Security")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateBookingWithXssPayload() {
        BookingRequest request = DataGenerator.getRandomBooking();
        request.setAdditionalneeds("<script>alert('xss')</script>");

        Response response = bookingClient.createBooking(request);
        AssertionUtils.assertStatusCode(response, HttpStatusCodes.OK);

        BookingResponse created = response.as(BookingResponse.class);
        assertThat(created.getBooking().getAdditionalneeds()).contains("<script>");
    }

    @Test(description = "Verify booking creation with inverted dates (checkout before checkin)")
    @Story("Create Booking - Business Logic")
    @Severity(SeverityLevel.NORMAL)
    public void testCreateBookingWithInvertedDates() {
        BookingRequest request = DataGenerator.getRandomBooking();
        request.setBookingdates(BookingDates.builder()
                .checkin("2026-12-30")
                .checkout("2026-12-01")
                .build());

        Response response = bookingClient.createBooking(request);
        // Even if the public API allows it or handles it, verify status code is returned
        assertThat(response.getStatusCode()).isIn(HttpStatusCodes.OK, HttpStatusCodes.BAD_REQUEST);
    }
}

