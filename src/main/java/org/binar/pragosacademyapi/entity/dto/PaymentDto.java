package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long userId;
    private String categoryName;
    private String courseName;
    private Boolean status;
    private String paymentMethod;
    private LocalDateTime paymentDate;
}
