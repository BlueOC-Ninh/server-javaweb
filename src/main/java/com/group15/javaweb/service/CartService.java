package com.group15.javaweb.service;

import com.group15.javaweb.dto.response.CartItemResponse;
import com.group15.javaweb.dto.response.CartResponse;
import com.group15.javaweb.entity.Cart;
import com.group15.javaweb.entity.CartItem;
import com.group15.javaweb.entity.Product;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.repository.CartItemRepository;
import com.group15.javaweb.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    public CartResponse getCartByUser(String userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(404,"Không tìm thấy giỏ hàng"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        BigDecimal totalOriginal = BigDecimal.ZERO;
        BigDecimal totalDiscounted = BigDecimal.ZERO;

        List<CartItemResponse> responses = new ArrayList<>();

        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            BigDecimal price = product.getPrice();
            BigDecimal discount = product.getDiscount() != null ? product.getDiscount() : BigDecimal.ZERO;
            BigDecimal discounted = price.subtract(discount);

            totalOriginal = totalOriginal.add(price);
            totalDiscounted = totalDiscounted.add(discounted);

            CartItemResponse response = new CartItemResponse();
            response.setId(item.getId());
            response.setProduct(product);
            response.setEmail(item.getEmail());
            response.setPassword(item.getPassword());

            responses.add(response);
        }

        CartResponse cartResponse = new CartResponse();
        cartResponse.setItems(responses);
        cartResponse.setTotalOriginalPrice(totalOriginal);
        cartResponse.setTotalDiscountedPrice(totalDiscounted);

        return cartResponse;
    }



}
