package com.group15.javaweb.dto.request;

import lombok.*;

import java.math.BigDecimal;

@Setter
@Getter
public class CreateQrRequest {
    private  String userId;
    private BigDecimal totalAmount;
}
