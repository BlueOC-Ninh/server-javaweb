package com.group15.javaweb.controller;

import com.group15.javaweb.dto.request.CreateQrRequest;
import com.group15.javaweb.dto.request.PayOSCallbackRequest;
import com.group15.javaweb.dto.response.ApiResponse;
import com.group15.javaweb.service.OrderService;
import com.group15.javaweb.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    private  final OrderService orderService;
    @PostMapping("/create")
    public ApiResponse<String> createPayment(@RequestBody CreateQrRequest createQrRequest) {
        String qrCodeUrl = paymentService.createPayment(createQrRequest);
        return ApiResponse.createdSuccess("Tạo link thanh toán thành công", qrCodeUrl);
    }

    @PostMapping("/callback")
    public ApiResponse<Void> paymentCallback(@RequestBody PayOSCallbackRequest callbackRequest) {
            orderService.createOrderFromPayment(callbackRequest.getUserId(), callbackRequest.getAmountPaid());
            return ApiResponse.success("Thanh toán thành công, đơn hàng đã được tạo",null);
    }
}
