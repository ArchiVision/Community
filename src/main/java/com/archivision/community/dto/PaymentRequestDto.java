package com.archivision.community.dto;

import lombok.Data;

@Data
public class PaymentRequestDto {
    private double total;
    private String currency;
}
