package com.enterprise.api.base;

import com.enterprise.api.clients.booker.BookerAuthClient;
import com.enterprise.api.clients.booker.BookingClient;
import com.enterprise.api.clients.dummyjson.CartClient;
import com.enterprise.api.clients.dummyjson.DummyAuthClient;
import com.enterprise.api.clients.dummyjson.ProductClient;
import com.enterprise.api.clients.dummyjson.UserClient;
import com.enterprise.api.clients.httpbin.HttpBinClient;
import com.enterprise.api.clients.reqres.ReqresClient;
import com.enterprise.api.config.ConfigurationManager;
import com.enterprise.api.listeners.TestListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Listeners;

@Listeners({TestListener.class})
public abstract class BaseTest {

    protected static final Logger log = LoggerFactory.getLogger(BaseTest.class);

    // Initialized Clients
    protected static BookerAuthClient bookerAuthClient;
    protected static BookingClient bookingClient;
    protected static DummyAuthClient dummyAuthClient;
    protected static ProductClient productClient;
    protected static UserClient userClient;
    protected static CartClient cartClient;
    protected static ReqresClient reqresClient;
    protected static HttpBinClient httpBinClient;

    // Shared Tokens
    protected static String bookerToken;
    protected static String dummyAccessToken;

    @BeforeSuite(alwaysRun = true)
    public void setupSuite() {
        log.info("Initializing Enterprise API Automation Test Suite...");
        log.info("Base URL (Booker): {}", ConfigurationManager.get().bookerBaseUrl());
        log.info("Base URL (DummyJSON): {}", ConfigurationManager.get().dummyJsonBaseUrl());
        log.info("Base URL (ReqRes): {}", ConfigurationManager.get().reqresBaseUrl());
        log.info("Base URL (HttpBin): {}", ConfigurationManager.get().httpbinBaseUrl());

        bookerAuthClient = new BookerAuthClient();
        bookingClient = new BookingClient();
        dummyAuthClient = new DummyAuthClient();
        productClient = new ProductClient();
        userClient = new UserClient();
        cartClient = new CartClient();
        reqresClient = new ReqresClient();
        httpBinClient = new HttpBinClient();

        // Warm up and retrieve auth tokens
        try {
            bookerToken = bookerAuthClient.getDefaultToken();
            log.info("Restful-Booker Token initialized successfully.");
        } catch (Exception e) {
            log.warn("Could not retrieve initial Booker token, will retry per-test: {}", e.getMessage());
        }

        try {
            var loginResp = dummyAuthClient.login(
                    ConfigurationManager.get().dummyJsonUsername(),
                    ConfigurationManager.get().dummyJsonPassword()
            );
            if (loginResp.getStatusCode() == 200) {
                dummyAccessToken = loginResp.jsonPath().getString("accessToken");
                log.info("DummyJSON Access Token initialized successfully.");
            }
        } catch (Exception e) {
            log.warn("Could not retrieve initial DummyJSON token: {}", e.getMessage());
        }
    }
}

