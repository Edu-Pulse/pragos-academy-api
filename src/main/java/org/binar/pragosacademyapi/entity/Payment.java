package org.binar.pragosacademyapi.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "payments")
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code")
    private Course course;
    private Long amount;
    @Column(name = "payment_method")
    private String paymentMethod;
    @Column(name = "card_number")
    private String cardNumber;
    @Column(name = "card_holder_name")
    private String cardHolderName;
    @Column(name = "cvv")
    private String cvv;
    @Column(name = "expiry_date")
    private String expiryDate;
    private Boolean status;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    private Integer rating;

}
