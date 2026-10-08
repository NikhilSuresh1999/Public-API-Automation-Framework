package com.enterprise.api.clients.booker;

import com.enterprise.api.clients.BaseClient;
import com.enterprise.api.constants.EndPoints;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.spec.RequestSpecBuilderFactory;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.util.Map;

import static io.restassured.RestAssured.given;

public class BookingClient extends BaseClient {

    private final RequestSpecification spec = RequestSpecBuilderFactory.getBookerSpec();

    @Step("Get list of all booking IDs")
    public Response getAllBookingIds() {
        return given().spec(spec)
                .when()
                .get(EndPoints.BOOKER_BOOKING);
    }

    @Step("Get booking IDs with query filters: {0}")
    public Response getBookingIdsWithFilter(Map<String, ?> queryParams) {
        return given().spec(spec)
                .queryParams(queryParams)
                .when()
                .get(EndPoints.BOOKER_BOOKING);
    }

    @Step("Get booking details for ID: {0}")
    public Response getBookingById(Object bookingId) {
        return given().spec(spec)
                .pathParam("id", bookingId)
                .when()
                .get(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Get booking details with custom Accept header: {1}")
    public Response getBookingByIdWithAcceptHeader(Object bookingId, String acceptHeader) {
        return given().spec(spec)
                .header("Accept", acceptHeader)
                .pathParam("id", bookingId)
                .when()
                .get(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Create a new booking")
    public Response createBooking(BookingRequest booking) {
        return given().spec(spec)
                .body(booking)
                .when()
                .post(EndPoints.BOOKER_BOOKING);
    }

    @Step("Create a booking with raw payload")
    public Response createBookingRaw(String rawPayload) {
        return given().spec(spec)
                .body(rawPayload)
                .when()
                .post(EndPoints.BOOKER_BOOKING);
    }

    @Step("Update booking ID: {0} using Auth Token: {1}")
    public Response updateBooking(Object bookingId, BookingRequest booking, String token) {
        RequestSpecification req = given().spec(spec).pathParam("id", bookingId).body(booking);
        if (token != null) {
            req.cookie("token", token);
        }
        return req.when().put(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Update booking ID: {0} using Basic Auth header")
    public Response updateBookingWithBasicAuth(Object bookingId, BookingRequest booking, String username, String password) {
        return given().spec(spec)
                .auth().preemptive().basic(username, password)
                .pathParam("id", bookingId)
                .body(booking)
                .when()
                .put(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Partial update (PATCH) booking ID: {0}")
    public Response partialUpdateBooking(Object bookingId, Map<String, Object> partialFields, String token) {
        RequestSpecification req = given().spec(spec).pathParam("id", bookingId).body(partialFields);
        if (token != null) {
            req.cookie("token", token);
        }
        return req.when().patch(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Delete booking ID: {0} using token: {1}")
    public Response deleteBooking(Object bookingId, String token) {
        RequestSpecification req = given().spec(spec).pathParam("id", bookingId);
        if (token != null) {
            req.cookie("token", token);
        }
        return req.when().delete(EndPoints.BOOKER_BOOKING_BY_ID);
    }

    @Step("Check health / ping endpoint")
    public Response ping() {
        return given().spec(spec)
                .when()
                .get(EndPoints.BOOKER_PING);
    }
}

