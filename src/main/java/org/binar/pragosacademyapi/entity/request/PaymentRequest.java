package org.binar.pragosacademyapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentRequest {
    private String cardNumber;
    private String cardHolderName;
    private String cvv;
    private String expiryDate;

}
