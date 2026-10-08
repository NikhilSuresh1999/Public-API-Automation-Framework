package com.enterprise.api.tests.booker;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker Microservice")
@Feature("Booking Deletion")
public class DeleteBookingTests extends BaseTest {

    @Test(description = "Verify successful deletion of an existing booking with valid token")
    @Story("Delete Booking")
    @Severity(SeverityLevel.BLOCKER)
    public void testDeleteBookingWithValidToken() {
        // 1. Create a fresh booking
        BookingRequest booking = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(booking).jsonPath().getInt("bookingid");

        // 2. Delete it
        Response deleteResp = bookingClient.deleteBooking(bookingId, bookerToken);
        AssertionUtils.assertStatusCode(deleteResp, HttpStatusCodes.CREATED); // Restful-booker returns 201 Created on delete
        AssertionUtils.assertResponseTimeWithinDefaultSla(deleteResp);

        // 3. Verify it no longer exists
        Response getResp = bookingClient.getBookingById(bookingId);
        AssertionUtils.assertStatusCode(getResp, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify 403 Forbidden when deleting booking without auth token")
    @Story("Security - Unauthorized Deletion")
    @Severity(SeverityLevel.BLOCKER)
    public void testDeleteBookingWithoutToken() {
        BookingRequest booking = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(booking).jsonPath().getInt("bookingid");

        Response deleteResp = bookingClient.deleteBooking(bookingId, null);
        AssertionUtils.assertStatusCode(deleteResp, HttpStatusCodes.FORBIDDEN);

        // Verify booking was NOT deleted
        Response getResp = bookingClient.getBookingById(bookingId);
        AssertionUtils.assertStatusCode(getResp, HttpStatusCodes.OK);
    }

    @Test(description = "Verify 403 Forbidden when deleting booking with invalid token")
    @Story("Security - Invalid Token Deletion")
    @Severity(SeverityLevel.CRITICAL)
    public void testDeleteBookingWithInvalidToken() {
        BookingRequest booking = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(booking).jsonPath().getInt("bookingid");

        Response deleteResp = bookingClient.deleteBooking(bookingId, "invalid_cookie_token_9999");
        AssertionUtils.assertStatusCode(deleteResp, HttpStatusCodes.FORBIDDEN);
    }

    @Test(description = "Verify deleting an already deleted booking returns 405 Method Not Allowed")
    @Story("Idempotency & Double Deletion")
    @Severity(SeverityLevel.NORMAL)
    public void testDoubleDeleteBooking() {
        BookingRequest booking = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(booking).jsonPath().getInt("bookingid");

        // First deletion
        bookingClient.deleteBooking(bookingId, bookerToken);

        // Second deletion
        Response secondDeleteResp = bookingClient.deleteBooking(bookingId, bookerToken);
        assertThat(secondDeleteResp.getStatusCode())
                .as("Verify double deletion returns 405 or 404")
                .isIn(HttpStatusCodes.METHOD_NOT_ALLOWED, HttpStatusCodes.NOT_FOUND);
    }

    @Test(description = "Verify deleting non-existent booking returns 405 Method Not Allowed")
    @Story("Delete Non-Existent")
    @Severity(SeverityLevel.NORMAL)
    public void testDeleteNonExistentBooking() {
        Response response = bookingClient.deleteBooking(999999999, bookerToken);
        assertThat(response.getStatusCode())
                .as("Verify non-existent deletion status")
                .isIn(HttpStatusCodes.METHOD_NOT_ALLOWED, HttpStatusCodes.NOT_FOUND);
    }
}

