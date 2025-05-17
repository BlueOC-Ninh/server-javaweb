package com.group15.javaweb.dto.response;

import com.group15.javaweb.entity.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CartItemResponse {
    private String id;
    private Product product;
    private String email;
    private String password;
}
