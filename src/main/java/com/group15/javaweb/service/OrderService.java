package com.group15.javaweb.service;

import com.group15.javaweb.entity.*;
import com.group15.javaweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;

    public void createOrderFromPayment(String userId, BigDecimal finalAmount) {
        // Lấy user theo userId
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Lấy giỏ hàng và các item trong giỏ
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        // Tính tổng tiền chưa giảm
        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal discount = item.getProduct().getDiscount();
            BigDecimal priceAfterDiscount = price.subtract(price.multiply(discount).divide(BigDecimal.valueOf(100)));
            totalAmount = totalAmount.add(priceAfterDiscount);
            discountAmount = discountAmount.add(discount);
        }

        // Tạo đơn hàng với tổng tiền, tiền giảm và tiền cuối cùng
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount); // tong tien truoc giam gia
        order.setDiscountAmount(discountAmount); // tong tien duoc giam
        order.setFinalAmount(finalAmount);  // Giá trị thanh toán thực tế từ PayOS
        orderRepository.save(order);

        // Tạo các orderItem dựa trên cartItems
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            BigDecimal price = product.getPrice();
            BigDecimal discount = product.getDiscount();
            BigDecimal priceAfterDiscount = price.subtract(price.multiply(discount).divide(BigDecimal.valueOf(100)));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setTotalPrice(priceAfterDiscount);

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteAll(cartItems);
    }
}
