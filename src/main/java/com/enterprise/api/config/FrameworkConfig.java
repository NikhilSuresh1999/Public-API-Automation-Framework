package com.enterprise.api.config;

import org.aeonbits.owner.Config;

@Config.LoadPolicy(Config.LoadType.MERGE)
@Config.Sources({
        "system:properties",
        "system:env",
        "classpath:config.properties"
})
public interface FrameworkConfig extends Config {

    @Key("booker.base.url")
    @DefaultValue("https://restful-booker.herokuapp.com")
    String bookerBaseUrl();

    @Key("dummyjson.base.url")
    @DefaultValue("https://dummyjson.com")
    String dummyJsonBaseUrl();

    @Key("reqres.base.url")
    @DefaultValue("https://reqres.in")
    String reqresBaseUrl();

    @Key("httpbin.base.url")
    @DefaultValue("https://httpbin.org")
    String httpbinBaseUrl();

    @Key("booker.username")
    @DefaultValue("admin")
    String bookerUsername();

    @Key("booker.password")
    @DefaultValue("password123")
    String bookerPassword();

    @Key("dummyjson.username")
    @DefaultValue("emilys")
    String dummyJsonUsername();

    @Key("dummyjson.password")
    @DefaultValue("emilyspass")
    String dummyJsonPassword();

    @Key("sla.max.response.time.ms")
    @DefaultValue("5000")
    long maxResponseTimeMs();

    @Key("retry.max.attempts")
    @DefaultValue("2")
    int maxRetryAttempts();

    @Key("log.requests")
    @DefaultValue("true")
    boolean logRequests();
}

