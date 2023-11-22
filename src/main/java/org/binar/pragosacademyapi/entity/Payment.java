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
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private User idUser;
    @ManyToOne
    @JoinColumn(name = "course_code", referencedColumnName = "code")
    private Course courseCode;
    private Long amount;
    @Column(name = "payment_method")
    private String paymentMethod;
    private Boolean status;
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;
    private int rating;

}
