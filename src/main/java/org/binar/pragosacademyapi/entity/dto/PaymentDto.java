package org.binar.pragosacademyapi.entity.dto;

import java.time.LocalDateTime;

public class PaymentDto {
    private Long userId;
    private String categoryName;
    private String courseName;
    private Boolean status;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}
