package com.enterprise.api.models.httpbin;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class HttpBinResponse {
    private Map<String, String> args;
    private Map<String, String> headers;
    private String origin;
    private String url;
    private String data;
    private Object json;
    private String userAgent;
    private Boolean authenticated;
    private String user;
    private String token;
}

