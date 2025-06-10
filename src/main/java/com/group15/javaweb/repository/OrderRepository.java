package com.group15.javaweb.repository;

import com.group15.javaweb.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    Page<Order> findAllByOrderDateBetween(LocalDateTime fromDate, LocalDateTime toDate, Pageable pageable);
    Page<Order> findAllByOrderDateGreaterThanEqual(LocalDateTime fromDate, Pageable pageable);
    Page<Order> findAllByOrderDateLessThanEqual(LocalDateTime toDate, Pageable pageable);


    @Query("SELECT DATE(o.orderDate) as orderDate, COUNT(o) as orderCount, SUM(o.finalAmount) as revenue " +
            "FROM Order o WHERE o.orderDate >= :startDate AND o.orderDate < :endDate " +
            "GROUP BY DATE(o.orderDate) ORDER BY DATE(o.orderDate)")
    List<Object[]> getDailyStatsForRange(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);
}
