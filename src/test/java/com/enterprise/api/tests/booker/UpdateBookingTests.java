package com.enterprise.api.tests.booker;

import com.enterprise.api.base.BaseTest;
import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.constants.HttpStatusCodes;
import com.enterprise.api.models.booker.BookingDates;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.utils.AssertionUtils;
import com.enterprise.api.utils.DataGenerator;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@Epic("Restful-Booker Microservice")
@Feature("Booking Updates (PUT & PATCH)")
public class UpdateBookingTests extends BaseTest {

    @Test(description = "Verify updating entire booking (PUT) with valid auth token")
    @Story("Full Update (PUT)")
    @Severity(SeverityLevel.BLOCKER)
    public void testFullUpdateBookingWithToken() {
        // 1. Create initial booking
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        // 2. Prepare update data
        BookingRequest updatedData = BookingRequest.builder()
                .firstname("UpdatedFirstName")
                .lastname("UpdatedLastName")
                .totalprice(1500)
                .depositpaid(true)
                .bookingdates(BookingDates.builder().checkin("2026-11-10").checkout("2026-11-20").build())
                .additionalneeds("VIP Executive Suite")
                .build();

        // 3. Perform PUT update
        Response updateResp = bookingClient.updateBooking(bookingId, updatedData, bookerToken);

        AssertionUtils.assertStatusCode(updateResp, HttpStatusCodes.OK);
        AssertionUtils.assertResponseTimeWithinDefaultSla(updateResp);

        BookingRequest responseBody = updateResp.as(BookingRequest.class);
        assertThat(responseBody.getFirstname()).isEqualTo("UpdatedFirstName");
        assertThat(responseBody.getLastname()).isEqualTo("UpdatedLastName");
        assertThat(responseBody.getTotalprice()).isEqualTo(1500);
        assertThat(responseBody.getAdditionalneeds()).isEqualTo("VIP Executive Suite");
    }

    @Test(description = "Verify updating entire booking (PUT) with Basic Authentication")
    @Story("Full Update (PUT) - Basic Auth")
    @Severity(SeverityLevel.CRITICAL)
    public void testFullUpdateBookingWithBasicAuth() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        initial.setFirstname("BasicAuthUpdatedName");
        Response updateResp = bookingClient.updateBookingWithBasicAuth(
                bookingId,
                initial,
                ConfigurationManager.get().bookerUsername(),
                ConfigurationManager.get().bookerPassword()
        );

        AssertionUtils.assertStatusCode(updateResp, HttpStatusCodes.OK);
        BookingRequest responseBody = updateResp.as(BookingRequest.class);
        assertThat(responseBody.getFirstname()).isEqualTo("BasicAuthUpdatedName");
    }

    @Test(description = "Verify 403 Forbidden when updating booking without auth token")
    @Story("Security - Unauthorized Update")
    @Severity(SeverityLevel.BLOCKER)
    public void testUpdateBookingWithoutToken() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        Response updateResp = bookingClient.updateBooking(bookingId, initial, null);
        AssertionUtils.assertStatusCode(updateResp, HttpStatusCodes.FORBIDDEN);
    }

    @Test(description = "Verify 403 Forbidden when updating booking with invalid auth token")
    @Story("Security - Invalid Token Update")
    @Severity(SeverityLevel.CRITICAL)
    public void testUpdateBookingWithInvalidToken() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        Response updateResp = bookingClient.updateBooking(bookingId, initial, "invalid_dummy_token_12345");
        AssertionUtils.assertStatusCode(updateResp, HttpStatusCodes.FORBIDDEN);
    }

    @Test(description = "Verify partial update (PATCH) of firstname and lastname")
    @Story("Partial Update (PATCH)")
    @Severity(SeverityLevel.BLOCKER)
    public void testPartialUpdateFirstnameAndLastname() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        Map<String, Object> partialPatch = Map.of(
                "firstname", "PatchedFirst",
                "lastname", "PatchedLast"
        );

        Response patchResp = bookingClient.partialUpdateBooking(bookingId, partialPatch, bookerToken);
        AssertionUtils.assertStatusCode(patchResp, HttpStatusCodes.OK);

        BookingRequest responseBody = patchResp.as(BookingRequest.class);
        assertThat(responseBody.getFirstname()).isEqualTo("PatchedFirst");
        assertThat(responseBody.getLastname()).isEqualTo("PatchedLast");
        // Ensure other fields remained unchanged
        assertThat(responseBody.getTotalprice()).isEqualTo(initial.getTotalprice());
    }

    @Test(description = "Verify partial update (PATCH) of depositpaid flag")
    @Story("Partial Update (PATCH)")
    @Severity(SeverityLevel.CRITICAL)
    public void testPartialUpdateDepositPaid() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        initial.setDepositpaid(false);
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        Map<String, Object> partialPatch = Map.of("depositpaid", true);

        Response patchResp = bookingClient.partialUpdateBooking(bookingId, partialPatch, bookerToken);
        AssertionUtils.assertStatusCode(patchResp, HttpStatusCodes.OK);

        BookingRequest responseBody = patchResp.as(BookingRequest.class);
        assertThat(responseBody.getDepositpaid()).isTrue();
    }

    @Test(description = "Verify 403 Forbidden when partial update (PATCH) without auth token")
    @Story("Security - Unauthorized PATCH")
    @Severity(SeverityLevel.CRITICAL)
    public void testPartialUpdateWithoutToken() {
        BookingRequest initial = DataGenerator.getRandomBooking();
        Integer bookingId = bookingClient.createBooking(initial).jsonPath().getInt("bookingid");

        Response patchResp = bookingClient.partialUpdateBooking(bookingId, Map.of("firstname", "Hacker"), null);
        AssertionUtils.assertStatusCode(patchResp, HttpStatusCodes.FORBIDDEN);
    }
}

