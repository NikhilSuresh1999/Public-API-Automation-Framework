package com.enterprise.api.models.reqres;

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
public class ReqresUserResponse {
    // Used for Create / Update responses
    private String id;
    private String name;
    private String job;
    private String createdAt;
    private String updatedAt;

    // Used for Single User GET response
    private UserData data;
    private Map<String, String> support;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class UserData {
        private Integer id;
        private String email;
        private String first_name;
        private String last_name;
        private String avatar;
    }
}

