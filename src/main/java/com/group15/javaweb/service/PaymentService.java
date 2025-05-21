package com.group15.javaweb.service;

import com.group15.javaweb.config.PayOSConfig;
import com.group15.javaweb.dto.request.CreateQrRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import vn.payos.PayOS;
import vn.payos.type.CheckoutResponseData;
import vn.payos.type.PaymentData;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PayOSConfig config;
    private final PayOS payOS;

    public String createPayment(CreateQrRequest createQrRequest) {
        try {
            long orderCode = System.currentTimeMillis();

            PaymentData paymentData = PaymentData.builder()
                    .orderCode(orderCode)
                    .amount(createQrRequest.getTotalAmount().intValue())
                    .description("Thanh toán")
                    .returnUrl(config.getReturnUrl())
                    .cancelUrl(config.getCancelUrl())
                    .signature(config.getChecksumKey())
                    .build();

            CheckoutResponseData responseData = payOS.createPaymentLink(paymentData);

            return responseData.getCheckoutUrl();

        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo link thanh toán: " + e.getMessage(), e);
        }
    }
}