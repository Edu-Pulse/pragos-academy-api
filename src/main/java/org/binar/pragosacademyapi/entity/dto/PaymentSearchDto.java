package org.binar.pragosacademyapi.entity.dto;

import lombok.Data;

@Data
public class PaymentSearchDto {
    private Long paymentId;
    private String courseName;
    private Long amount;
}
