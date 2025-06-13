//package com.group15.javaweb.service;
//
//import com.group15.javaweb.entity.*;
//import com.group15.javaweb.exception.ApiException;
//import com.group15.javaweb.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.math.BigDecimal;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class OrderService {
//
//    private final OrderRepository orderRepository;
//    private final UserRepository userRepository;
//    private final CartRepository cartRepository;
//    private final CartItemRepository cartItemRepository;
//    private final OrderItemRepository orderItemRepository;
//
//    public void createOrderFromPayment(String userId, BigDecimal finalAmount) {
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new ApiException(404, "Không tìm thấy người dùng"));
//
//        Cart cart = cartRepository.findByUserId(userId)
//                .orElseThrow(() -> new ApiException(404, "Không tìm thấy giỏ hàng"));
//
//        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());
//
//        if ( cartItems.isEmpty()) {
//            throw new ApiException(404, "Giỏ hàng hiện đang trống");
//        }
//
//        // Tính tổng tiền và giảm giá
//        BigDecimal totalAmount = BigDecimal.ZERO; // Tong tien phai tra truoc giam gia
//        BigDecimal discountAmount = BigDecimal.ZERO; // Tong tien giam gia
//
//        for (CartItem item : cartItems) {
//            BigDecimal price = item.getProduct().getPrice();
//            BigDecimal discount = item.getProduct().getDiscount();
//            if (discount == null) discount = BigDecimal.ZERO;
//            totalAmount = totalAmount.add(price);
//            discountAmount = discountAmount.add(price.multiply(discount).divide(BigDecimal.valueOf(100)));
//        }
//
//        // Tạo đơn hàng
//        Order order = new Order();
//        order.setUser(user);
//        order.setTotalAmount(totalAmount);
//        order.setDiscountAmount(discountAmount);
//        order.setFinalAmount(finalAmount);
//        orderRepository.save(order);
//
//        // Tạo các mục trong đơn hàng
//        for (CartItem item : cartItems) {
//            Product product = item.getProduct();
//            BigDecimal price = product.getPrice();
//            BigDecimal discount = product.getDiscount();
//            if (discount == null) discount = BigDecimal.ZERO;
//
//            BigDecimal priceAfterDiscount = price.subtract(price.multiply(discount).divide(BigDecimal.valueOf(100)));
//
//            OrderItem orderItem = new OrderItem();
//            orderItem.setOrder(order);
//            orderItem.setProduct(product);
//            orderItem.setTotalPrice(priceAfterDiscount); // gia sau khi giam
//
//            orderItemRepository.save(orderItem);
//        }
//
//        // Xóa toàn bộ giỏ hàng sau khi tạo đơn hàng
//        cartItemRepository.deleteAll(cartItems);
//    }
//}

package com.group15.javaweb.service;

import com.group15.javaweb.dto.response.OrderItemResponse;
import com.group15.javaweb.dto.response.OrderResponse;
import com.group15.javaweb.entity.*;
import com.group15.javaweb.exception.ApiException;
import com.group15.javaweb.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductRepository productRepository;

    public Page<OrderResponse> getOrders(LocalDateTime fromDate, LocalDateTime toDate, int page, int size) {
        PageRequest pageable = PageRequest.of(page, size, Sort.by("orderDate").descending());

        Page<Order> orders;

        if (fromDate != null && toDate != null) {
            orders = orderRepository.findAllByOrderDateBetween(fromDate, toDate, pageable);
        } else if (fromDate != null) {
            orders = orderRepository.findAllByOrderDateGreaterThanEqual(fromDate, pageable);
        } else if (toDate != null) {
            orders = orderRepository.findAllByOrderDateLessThanEqual(toDate, pageable);
        } else {
            orders = orderRepository.findAll(pageable);
        }

        return orders.map(this::convertToDto);
    }

    private OrderResponse convertToDto(Order order) {
        OrderResponse dto = new OrderResponse();
        dto.setId(order.getId());
        dto.setOrderDate(order.getOrderDate());
        dto.setTotalAmount(order.getTotalAmount());
        dto.setDiscountAmount(order.getDiscountAmount());
        dto.setFinalAmount(order.getFinalAmount());

        List<OrderItemResponse> itemDtos = order.getOrderItems().stream().map(item -> {
            Product product = item.getProduct();
            OrderItemResponse itemDto = new OrderItemResponse();
            itemDto.setProductId(product.getId());
            itemDto.setProductName(product.getName());
            itemDto.setProductImageUrl(product.getAvatarUrl());
            itemDto.setPrice(product.getPrice());
            itemDto.setDiscount(product.getDiscount());
            itemDto.setTotalPrice(item.getTotalPrice());
            return itemDto;
        }).collect(Collectors.toList());

        dto.setItems(itemDtos);
        return dto;
    }


    public void createOrderFromPayment(String userId, BigDecimal finalAmount) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(404, "Không tìm thấy người dùng"));

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(404, "Không tìm thấy giỏ hàng"));

        List<CartItem> cartItems = cartItemRepository.findByCartId(cart.getId());

        if (cartItems.isEmpty()) {
            throw new ApiException(404, "Giỏ hàng hiện đang trống");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;
        BigDecimal discountAmount = BigDecimal.ZERO;

        for (CartItem item : cartItems) {
            BigDecimal price = item.getProduct().getPrice();
            BigDecimal discount = item.getProduct().getDiscount();
            if (discount == null) discount = BigDecimal.ZERO;

            totalAmount = totalAmount.add(price);
            discountAmount = discountAmount.add(price.multiply(discount).divide(BigDecimal.valueOf(100)));
        }

        // Tạo đơn hàng
        Order order = new Order();
        order.setUser(user);
        order.setTotalAmount(totalAmount);
        order.setDiscountAmount(discountAmount);
        order.setFinalAmount(finalAmount);
        orderRepository.save(order);

        // Tạo OrderItem và cập nhật sản phẩm
        for (CartItem item : cartItems) {
            Product product = item.getProduct();
            BigDecimal price = product.getPrice();
            BigDecimal discount = product.getDiscount();
            if (discount == null) discount = BigDecimal.ZERO;

            BigDecimal priceAfterDiscount = price.subtract(price.multiply(discount).divide(BigDecimal.valueOf(100)));

            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setProduct(product);
            orderItem.setTotalPrice(priceAfterDiscount);
            orderItemRepository.save(orderItem);

            // Giảm tồn kho và tăng soldCount
            product.setQuantity(Math.max(0, product.getQuantity() - 1));
            product.setSoldCount(product.getSoldCount() + 1);
            productRepository.save(product);
        }

        cartItemRepository.deleteAll(cartItems);
    }
}
