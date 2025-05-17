package com.group15.javaweb.controller;

import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.dto.response.CartItemResponse;
import com.group15.javaweb.dto.response.CartResponse;
import com.group15.javaweb.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    @Autowired
    private CartService cartService;

    @GetMapping("/user/{userId}")
    public ApiResponse<CartResponse> getCartByUserId(@PathVariable String userId) {
        return  ApiResponse.success("Lấy thông tin giỏ hàng thành công",cartService.getCartByUser(userId));
    }
}
