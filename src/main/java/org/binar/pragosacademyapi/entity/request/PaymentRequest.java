package org.binar.pragosacademyapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private User userId;
    private Course courseName;
    private Long amount;
    private String paymentMethod;
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;
}
