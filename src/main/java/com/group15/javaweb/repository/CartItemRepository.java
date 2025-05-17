package com.group15.javaweb.repository;

import com.group15.javaweb.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, String> {
    List<CartItem> findByCartId(String cartId);
    Optional<CartItem> findByCartIdAndProductId(String cartId, String productId);
}
