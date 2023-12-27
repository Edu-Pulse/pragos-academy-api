package org.binar.pragosacademyapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.enumeration.PaymentMethod;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;
    private Long amount;
    private User userId;
    private Course courseCode;
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private LocalDate expiryDate;
}
