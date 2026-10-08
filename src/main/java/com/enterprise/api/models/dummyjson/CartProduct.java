package com.enterprise.api.models.dummyjson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartProduct {
    private Integer id;
    private String title;
    private Double price;
    private Integer quantity;
    private Double total;
    private Double discountPercentage;
    private Double discountedTotal;
    private String thumbnail;
}

