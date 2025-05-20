package com.group15.javaweb.dto.request;


import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
public class PayOSCallbackRequest {
    private  String userId;
    private BigDecimal amountPaid;
}
