package com.group15.javaweb.service;

import com.group15.javaweb.dto.request.CartItemRequest;
import com.group15.javaweb.dto.response.CartItemResponse;
import com.group15.javaweb.entity.Cart;
import com.group15.javaweb.entity.CartItem;
import com.group15.javaweb.entity.Product;
import com.group15.javaweb.entity.User;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.repository.CartItemRepository;
import com.group15.javaweb.repository.CartRepository;
import com.group15.javaweb.repository.ProductRepository;
import com.group15.javaweb.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class CartItemService {

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;


    public void deleteCartItemById(String cartItemId, String userId) {
        CartItem item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ApiException(400,"Không tìm thấy sản phẩm trong giỏ hàng"));

        Cart cart = item.getCart();

        if (!cart.getUser().getId().equals(userId)) {
            throw new ApiException(401,"Bạn không có quyền để xóa sản phẩm này khỏi giỏ hàng");
        }

        cart.setTotalPrice(cart.getTotalPrice().subtract(item.getProduct().getPrice()));
        cartRepository.save(cart);

        cartItemRepository.delete(item);
    }

    public CartItemResponse addToCart(CartItemRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new ApiException(404,"Không tìm thấy người dùng"));

        // Tìm cart theo user, nếu không có thì tạo mới
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    newCart.setTotalPrice(BigDecimal.ZERO);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new ApiException(404,"Không tìm thấy sản phẩm"));

        boolean exists = cartItemRepository
                .findByCartIdAndProductId(cart.getId(), product.getId())
                .isPresent();

        if (exists) {
            throw new ApiException(400,"Sản phẩm đã tồn tại trong giỏ hàng");
        }

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        CartItem cartItem = new CartItem();
        cartItem.setCart(cart);
        cartItem.setProduct(product);
        cartItem.setEmail(request.getEmail());
        cartItem.setPassword(passwordEncoder.encode(request.getPassword()));

        cartItemRepository.save(cartItem);

        cart.setTotalPrice(cart.getTotalPrice().add(product.getPrice()));
        cartRepository.save(cart);

        CartItemResponse response = new CartItemResponse();
        response.setId(cartItem.getId());
        response.setProduct(product);
        response.setEmail(cartItem.getEmail());
        response.setPassword(cartItem.getPassword());

        return response;
    }

}
