package com.group15.javaweb.controller;

import com.group15.javaweb.dto.response.OrderResponse;
import com.group15.javaweb.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderQueryService;

    
    //    http://127.1.1:8080/api/orders?page=0&size=5&fromDate=2024-05-31T13:03:18&toDate=2026-05-31T13:03:18
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public Page<OrderResponse> getOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate
    ) {
        return orderQueryService.getOrders(fromDate, toDate, page, size);
    }
}
