package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.enumeration.PaymentMethod;
import org.binar.pragosacademyapi.service.PaymentGatewayService;


public class PaymentGatewayServiceImpl implements PaymentGatewayService {

    @Override
    public String initiatePayment(Payment payment) {
        PaymentMethod paymentMethod = payment.getPaymentMethod();
        switch (paymentMethod) {
            case CREDIT_CARD:
                return initiateCreditCardPayment(payment);
            case DEBIT_CARD:
                return initiateDebitCardPayment(payment);
            case BANK_TRANSFER:
                return initiateBankTransferPayment(payment);
            case PAYPAL:
                return initiatePayPalPayment(payment);
            default:
                throw new IllegalArgumentException("Unsupported payment method: " + paymentMethod);
        }
    }

    private String initiatePayPalPayment(Payment payment) {
        return "PayPal Payment initiated for transaction ID: " + payment.getTransactionId();
    }

    private String initiateBankTransferPayment(Payment payment) {
        return "Bank Transfer initiated for transaction ID: " + payment.getTransactionId();
    }

    private String initiateDebitCardPayment(Payment payment) {
        return "Debit Card Payment initiated for transaction ID: " + payment.getTransactionId();
    }

    private String initiateCreditCardPayment(Payment payment) {
        return "Credit Card Payment initiated for transaction ID: " + payment.getTransactionId();
    }

}
