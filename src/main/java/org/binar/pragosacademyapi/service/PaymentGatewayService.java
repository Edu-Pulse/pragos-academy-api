package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.Payment;

public interface PaymentGatewayService {
    String initiatePayment(Payment payment);
}
