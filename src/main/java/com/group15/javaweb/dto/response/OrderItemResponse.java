package com.group15.javaweb.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class OrderItemResponse {
    private String productId;
    private String productName;
    private String productImageUrl;
    private BigDecimal price;
    private BigDecimal discount;
    private BigDecimal totalPrice;
}
