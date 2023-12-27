package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.enumeration.PaymentMethod;

import javax.persistence.Column;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDto {
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

    public PaymentDto(PaymentMethod paymentMethod, Long amount, User userId, Course courseCode) {
        this.paymentMethod = paymentMethod;
        this.amount = amount;
        this.userId = userId;
        this.courseCode = courseCode;
    }

}
