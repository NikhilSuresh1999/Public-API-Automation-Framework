package com.enterprise.api.tests.e2e;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.booker.BookingDates;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.models.booker.BookingResponse;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("End-to-End Business Scenarios")
@Feature("Booking Lifecycle Management")
public class E2EBookingLifecycleTest extends BaseTest {

    @Test(description = "E2E Complete Lifecycle: Auth -> Create -> Read -> Full Update -> Partial Update -> Delete -> Verify 404")
    @Story("Complete Booking Flow")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Executes full lifecycle of a hotel reservation from booking creation to deletion and verification.")
    public void testCompleteBookingLifecycle() {
        // Step 1: Authenticate and obtain active token
        Allure.step("Step 1: Authenticate and obtain token");
        String token = bookerAuthClient.getDefaultToken();
        assertThat(token).isNotBlank();

        // Step 2: Create a new booking
        Allure.step("Step 2: Create a new booking");
        BookingRequest initialBooking = DataGenerator.getRandomBooking();
        Response createResp = bookingClient.createBooking(initialBooking);
        AssertionUtils.assertStatusCode(createResp, HttpStatusCodes.OK);

        BookingResponse created = createResp.as(BookingResponse.class);
        Integer bookingId = created.getBookingid();
        assertThat(bookingId).isPositive();

        // Step 3: Retrieve and verify created booking details
        Allure.step("Step 3: Retrieve and verify booking details");
        Response getResp = bookingClient.getBookingById(bookingId);
        AssertionUtils.assertStatusCode(getResp, HttpStatusCodes.OK);
        BookingRequest retrieved = getResp.as(BookingRequest.class);
        assertThat(retrieved.getFirstname()).isEqualTo(initialBooking.getFirstname());
        assertThat(retrieved.getLastname()).isEqualTo(initialBooking.getLastname());

        // Step 4: Perform full update (PUT)
        Allure.step("Step 4: Perform full update (PUT)");
        BookingRequest fullUpdate = BookingRequest.builder()
                .firstname("E2EUpdatedFirst")
                .lastname("E2EUpdatedLast")
                .totalprice(1890)
                .depositpaid(true)
                .bookingdates(BookingDates.builder().checkin("2026-12-20").checkout("2026-12-28").build())
                .additionalneeds("Champagne on arrival")
                .build();

        Response updateResp = bookingClient.updateBooking(bookingId, fullUpdate, token);
        AssertionUtils.assertStatusCode(updateResp, HttpStatusCodes.OK);
        BookingRequest afterPut = updateResp.as(BookingRequest.class);
        assertThat(afterPut.getFirstname()).isEqualTo("E2EUpdatedFirst");
        assertThat(afterPut.getTotalprice()).isEqualTo(1890);

        // Step 5: Perform partial update (PATCH)
        Allure.step("Step 5: Perform partial update (PATCH)");
        Response patchResp = bookingClient.partialUpdateBooking(bookingId, Map.of("additionalneeds", "Presidential Suite Setup"), token);
        AssertionUtils.assertStatusCode(patchResp, HttpStatusCodes.OK);
        BookingRequest afterPatch = patchResp.as(BookingRequest.class);
        assertThat(afterPatch.getAdditionalneeds()).isEqualTo("Presidential Suite Setup");

        // Step 6: Delete booking
        Allure.step("Step 6: Delete booking");
        Response deleteResp = bookingClient.deleteBooking(bookingId, token);
        AssertionUtils.assertStatusCode(deleteResp, HttpStatusCodes.CREATED);

        // Step 7: Verify booking is no longer available (404 Not Found)
        Allure.step("Step 7: Verify booking is no longer available (404)");
        Response verifyGet = bookingClient.getBookingById(bookingId);
        AssertionUtils.assertStatusCode(verifyGet, HttpStatusCodes.NOT_FOUND);
    }
}

