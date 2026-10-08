package com.enterprise.api.constants;

public final class EndPoints {

    private EndPoints() {
        // Prevent instantiation
    }

    // ==========================================
    // Restful-Booker Endpoints
    // ==========================================
    public static final String BOOKER_AUTH = "/auth";
    public static final String BOOKER_BOOKING = "/booking";
    public static final String BOOKER_BOOKING_BY_ID = "/booking/{id}";
    public static final String BOOKER_PING = "/ping";

    // ==========================================
    // DummyJSON Endpoints
    // ==========================================
    public static final String DUMMY_AUTH_LOGIN = "/auth/login";
    public static final String DUMMY_AUTH_ME = "/auth/me";
    public static final String DUMMY_AUTH_REFRESH = "/auth/refresh";

    public static final String DUMMY_PRODUCTS = "/products";
    public static final String DUMMY_PRODUCT_BY_ID = "/products/{id}";
    public static final String DUMMY_PRODUCT_SEARCH = "/products/search";
    public static final String DUMMY_PRODUCT_CATEGORIES = "/products/categories";
    public static final String DUMMY_PRODUCT_BY_CATEGORY = "/products/category/{category}";
    public static final String DUMMY_PRODUCT_ADD = "/products/add";

    public static final String DUMMY_USERS = "/users";
    public static final String DUMMY_USER_BY_ID = "/users/{id}";
    public static final String DUMMY_USER_SEARCH = "/users/search";
    public static final String DUMMY_USER_FILTER = "/users/filter";

    public static final String DUMMY_CARTS = "/carts";
    public static final String DUMMY_CART_BY_ID = "/carts/{id}";
    public static final String DUMMY_CARTS_BY_USER = "/carts/user/{userId}";
    public static final String DUMMY_CART_ADD = "/carts/add";

    // ==========================================
    // ReqRes Endpoints
    // ==========================================
    public static final String REQRES_USERS = "/api/users";
    public static final String REQRES_USER_BY_ID = "/api/users/{id}";
    public static final String REQRES_REGISTER = "/api/register";
    public static final String REQRES_LOGIN = "/api/login";
    public static final String REQRES_UNKNOWN = "/api/unknown";
    public static final String REQRES_UNKNOWN_BY_ID = "/api/unknown/{id}";

    // ==========================================
    // HttpBin Endpoints
    // ==========================================
    public static final String HTTPBIN_STATUS = "/status/{code}";
    public static final String HTTPBIN_HEADERS = "/headers";
    public static final String HTTPBIN_IP = "/ip";
    public static final String HTTPBIN_USER_AGENT = "/user-agent";
    public static final String HTTPBIN_GET = "/get";
    public static final String HTTPBIN_POST = "/post";
    public static final String HTTPBIN_PUT = "/put";
    public static final String HTTPBIN_PATCH = "/patch";
    public static final String HTTPBIN_DELETE = "/delete";
    public static final String HTTPBIN_BASIC_AUTH = "/basic-auth/{user}/{passwd}";
    public static final String HTTPBIN_BEARER = "/bearer";
}

