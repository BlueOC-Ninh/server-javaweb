package com.group15.javaweb.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
public class CartResponse {
    private List<CartItemResponse> items;
    private BigDecimal totalOriginalPrice;
    private BigDecimal totalDiscountedPrice;
}
