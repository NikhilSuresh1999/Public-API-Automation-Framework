package com.enterprise.api.utils;

import com.enterprise.api.models.booker.BookingDates;
import com.enterprise.api.models.booker.BookingRequest;
import com.enterprise.api.models.dummyjson.CartRequest;
import com.enterprise.api.models.dummyjson.ProductRequest;
import com.enterprise.api.models.reqres.ReqresUserRequest;
import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

public final class DataGenerator {

    private static final Faker faker = new Faker(Locale.ENGLISH);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private DataGenerator() {
        // Prevent instantiation
    }

    public static Faker getFaker() {
        return faker;
    }

    public static BookingRequest getRandomBooking() {
        LocalDate checkin = LocalDate.now().plusDays(faker.number().numberBetween(1, 30));
        LocalDate checkout = checkin.plusDays(faker.number().numberBetween(2, 14));

        return BookingRequest.builder()
                .firstname(faker.name().firstName())
                .lastname(faker.name().lastName())
                .totalprice(faker.number().numberBetween(100, 5000))
                .depositpaid(faker.bool().bool())
                .bookingdates(BookingDates.builder()
                        .checkin(checkin.format(DATE_FORMATTER))
                        .checkout(checkout.format(DATE_FORMATTER))
                        .build())
                .additionalneeds(faker.options().option("Breakfast", "Airport Shuttle", "Late Checkout", "Sea View Room"))
                .build();
    }

    public static ProductRequest getRandomProduct() {
        return ProductRequest.builder()
                .title(faker.commerce().productName())
                .description(faker.lorem().paragraph(2))
                .price(faker.number().randomDouble(2, 10, 2000))
                .discountPercentage(faker.number().randomDouble(2, 1, 30))
                .rating(faker.number().randomDouble(2, 3, 5))
                .stock(faker.number().numberBetween(5, 500))
                .brand(faker.company().name())
                .category("smartphones")
                .thumbnail(faker.internet().image())
                .images(List.of(faker.internet().image(), faker.internet().image()))
                .build();
    }

    public static ReqresUserRequest getRandomReqresUser() {
        return ReqresUserRequest.builder()
                .name(faker.name().fullName())
                .job(faker.job().title())
                .build();
    }

    public static CartRequest getRandomCart(int userId) {
        return CartRequest.builder()
                .userId(userId)
                .products(List.of(
                        CartRequest.CartItemInput.builder()
                                .id(faker.number().numberBetween(1, 20))
                                .quantity(faker.number().numberBetween(1, 5))
                                .build(),
                        CartRequest.CartItemInput.builder()
                                .id(faker.number().numberBetween(21, 50))
                                .quantity(faker.number().numberBetween(1, 3))
                                .build()
                ))
                .build();
    }
}

