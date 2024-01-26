package com.archivision.community.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentRequestDto {
    private double total;
    private String currency;
}
