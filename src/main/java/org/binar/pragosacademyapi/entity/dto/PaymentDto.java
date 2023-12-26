package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.PaymentMethod;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
    private Long userId;
    private String categoryName;
    private String courseName;
    private Boolean status;
    private PaymentMethod paymentMethod;
    private LocalDateTime paymentDate;
}
