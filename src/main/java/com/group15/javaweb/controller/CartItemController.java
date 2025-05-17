package com.group15.javaweb.controller;

import com.cloudinary.Api;
import com.group15.javaweb.dto.request.CartItemRequest;
import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.dto.response.CartItemResponse;
import com.group15.javaweb.service.CartItemService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart-items")
public class CartItemController {

    @Autowired
    private CartItemService cartItemService;

    @PostMapping
    public ApiResponse<CartItemResponse> addToCart(@Valid @RequestBody CartItemRequest request) {
        return ApiResponse.success("Thêm sản phẩm vào giỏ hàng thành công",cartItemService.addToCart(request));
    }

    @DeleteMapping("/{cartItemId}")
    public ApiResponse<Void> deleteCartItem(@PathVariable String cartItemId, @RequestParam String userId) {
        cartItemService.deleteCartItemById(cartItemId, userId);
        return ApiResponse.success("Xóa sản phẩm khỏi giỏ hàng thành công",null );
    }
}
