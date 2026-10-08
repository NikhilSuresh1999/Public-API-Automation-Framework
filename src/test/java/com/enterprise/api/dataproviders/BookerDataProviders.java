package com.enterprise.api.dataproviders;

import com.enterprise.api.models.booker.BookingDates;
import com.enterprise.api.models.booker.BookingRequest;
import org.testng.annotations.DataProvider;

public final class BookerDataProviders {

    private BookerDataProviders() {
        // Prevent instantiation
    }

    @DataProvider(name = "authInvalidCredentials")
    public static Object[][] authInvalidCredentials() {
        return new Object[][]{
                {"invalidUser", "password123", "Bad credentials"},
                {"admin", "wrongPassword", "Bad credentials"},
                {"", "password123", "Bad credentials"},
                {"admin", "", "Bad credentials"},
                {"", "", "Bad credentials"},
                {"' OR '1'='1", "' OR '1'='1", "Bad credentials"},
                {"admin", "pass<script>alert(1)</script>", "Bad credentials"}
        };
    }

    @DataProvider(name = "validBookingPayloads")
    public static Object[][] validBookingPayloads() {
        return new Object[][]{
                {BookingRequest.builder()
                        .firstname("James").lastname("Bond").totalprice(999).depositpaid(true)
                        .bookingdates(BookingDates.builder().checkin("2026-11-01").checkout("2026-11-07").build())
                        .additionalneeds("Martini, shaken not stirred").build()},
                {BookingRequest.builder()
                        .firstname("Sherlock").lastname("Holmes").totalprice(250).depositpaid(false)
                        .bookingdates(BookingDates.builder().checkin("2026-12-01").checkout("2026-12-05").build())
                        .additionalneeds("Violin practice room").build()},
                {BookingRequest.builder()
                        .firstname("Bruce").lastname("Wayne").totalprice(50000).depositpaid(true)
                        .bookingdates(BookingDates.builder().checkin("2027-01-10").checkout("2027-01-20").build())
                        .additionalneeds("Helipad parking").build()},
                {BookingRequest.builder()
                        .firstname("Peter").lastname("Parker").totalprice(50).depositpaid(false)
                        .bookingdates(BookingDates.builder().checkin("2026-11-15").checkout("2026-11-16").build())
                        .additionalneeds("Roof access").build()}
        };
    }

    @DataProvider(name = "invalidBookingPayloads")
    public static Object[][] invalidBookingPayloads() {
        return new Object[][]{
                {BookingRequest.builder()
                        .firstname(null).lastname("Doe").totalprice(100).depositpaid(true)
                        .bookingdates(BookingDates.builder().checkin("2026-11-01").checkout("2026-11-05").build()).build(),
                        "Missing Firstname"},
                {BookingRequest.builder()
                        .firstname("John").lastname(null).totalprice(100).depositpaid(true)
                        .bookingdates(BookingDates.builder().checkin("2026-11-01").checkout("2026-11-05").build()).build(),
                        "Missing Lastname"},
                {BookingRequest.builder()
                        .firstname("John").lastname("Doe").totalprice(null).depositpaid(true)
                        .bookingdates(BookingDates.builder().checkin("2026-11-01").checkout("2026-11-05").build()).build(),
                        "Missing TotalPrice"},
                {BookingRequest.builder()
                        .firstname("John").lastname("Doe").totalprice(100).depositpaid(null)
                        .bookingdates(BookingDates.builder().checkin("2026-11-01").checkout("2026-11-05").build()).build(),
                        "Missing DepositPaid"},
                {BookingRequest.builder()
                        .firstname("John").lastname("Doe").totalprice(100).depositpaid(true)
                        .bookingdates(null).build(),
                        "Missing BookingDates"}
        };
    }

    @DataProvider(name = "searchFilterQueries")
    public static Object[][] searchFilterQueries() {
        return new Object[][]{
                {"checkin", "2026-01-01"},
                {"checkout", "2027-01-01"},
                {"firstname", "James"},
                {"lastname", "Bond"},
                {"firstname", "NonExistentName987654"}
        };
    }
}

