package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentUserDto {
    private String categoryName;
    private String courseName;
    private Boolean status;
    private String paymentMethod;
    private Long amount;
    private LocalDateTime paymentDate;
}
