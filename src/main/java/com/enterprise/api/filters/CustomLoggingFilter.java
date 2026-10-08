package com.enterprise.api.filters;

import io.restassured.filter.Filter;
import io.restassured.filter.FilterContext;
import io.restassured.response.Response;
import io.restassured.specification.FilterableRequestSpecification;
import io.restassured.specification.FilterableResponseSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

public class CustomLoggingFilter implements Filter {

    private static final Logger log = LoggerFactory.getLogger(CustomLoggingFilter.class);
    private static final Set<String> SENSITIVE_HEADERS = Set.of("authorization", "cookie", "x-api-key");

    @Override
    public Response filter(FilterableRequestSpecification requestSpec,
                           FilterableResponseSpecification responseSpec,
                           FilterContext ctx) {
        if (log.isDebugEnabled() || log.isInfoEnabled()) {
            log.info("====== HTTP REQUEST ======");
            log.info("Method: {}", requestSpec.getMethod());
            log.info("URI: {}", requestSpec.getURI());

            requestSpec.getHeaders().forEach(header -> {
                String headerName = header.getName().toLowerCase();
                String headerVal = SENSITIVE_HEADERS.contains(headerName) ? "******" : header.getValue();
                log.info("Header -> {}: {}", header.getName(), headerVal);
            });

            if (requestSpec.getBody() != null) {
                log.info("Payload: {}", requestSpec.getBody().toString());
            }
        }

        Response response = ctx.next(requestSpec, responseSpec);

        if (log.isDebugEnabled() || log.isInfoEnabled()) {
            log.info("====== HTTP RESPONSE ======");
            log.info("Status Code: {}", response.getStatusCode());
            log.info("Response Time: {} ms", response.getTime());
            if (response.getBody() != null && response.getBody().asString().length() < 2000) {
                log.info("Body: {}", response.getBody().asString());
            } else if (response.getBody() != null) {
                log.info("Body (truncated): {}...", response.getBody().asString().substring(0, 500));
            }
            log.info("===========================");
        }

        return response;
    }
}

