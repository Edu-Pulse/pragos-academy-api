package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.springframework.data.domain.Page;

public interface PaymentService {
    Response<Page<PaymentDto>> getPaymentsByType(int page);
    Response<Page<PaymentDto>> searchPaymentsByCourseName(String courseName, int page);
    Response<Page<PaymentUserDto>> getPaymentByUser(int page);
    Response<PaymentDto> createTransferBank(PaymentRequest paymentRequest);
    Response<PaymentDto> createCreditCard(PaymentRequest paymentRequest);
}
