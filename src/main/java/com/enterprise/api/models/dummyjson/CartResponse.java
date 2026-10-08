package com.enterprise.api.models.dummyjson;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CartResponse {
    private Integer id;
    private List<CartProduct> products;
    private Double total;
    private Double discountedTotal;
    private Integer userId;
    private Integer totalProducts;
    private Integer totalQuantity;
    private Boolean isDeleted;
    private String deletedOn;
}

