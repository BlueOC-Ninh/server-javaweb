package com.group15.javaweb.repository;

import com.group15.javaweb.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    // Nếu cần truy vấn thêm theo orderCode (ví dụ dùng UUID hoặc định dạng riêng):
    // Optional<Order> findByOrderCode(String orderCode);
}
