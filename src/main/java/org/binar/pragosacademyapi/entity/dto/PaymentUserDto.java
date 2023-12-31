package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.binar.pragosacademyapi.enumeration.Level;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentUserDto {
    private String image;
    private String categoryName;
    private String courseName;
    private String lecturer;
    private Level level;
    private Boolean status;
    private String paymentMethod;
    private Long amount;
    private LocalDateTime paymentDate;
}
