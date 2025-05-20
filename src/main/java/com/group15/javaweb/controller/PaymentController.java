package com.group15.javaweb.controller;

import com.group15.javaweb.dto.request.CreateQrRequest;
import com.group15.javaweb.dto.request.PayOSCallbackRequest;
import com.group15.javaweb.entity.Order;
import com.group15.javaweb.service.OrderService;
import com.group15.javaweb.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Objects;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private  final OrderService orderService;
    @PostMapping("/create")
    public ResponseEntity<String> createPayment(@RequestBody CreateQrRequest createQrRequest) {
        String qrCodeUrl = paymentService.createPayment(createQrRequest);
        return ResponseEntity.ok(qrCodeUrl);
    }

    // 2. Callback từ PayOS báo thanh toán thành công
    @PostMapping("/callback")
    public ResponseEntity<String> paymentCallback(@RequestBody PayOSCallbackRequest callbackRequest) {
            orderService.createOrderFromPayment(callbackRequest.getUserId(), callbackRequest.getAmountPaid());
            return ResponseEntity.ok("Payment processed");
    }
}
