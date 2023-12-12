package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.springframework.data.domain.Page;

public interface PaymentService {
    Response<Page<PaymentDto>> getPaymentsByType(int page);
    Response<Page<PaymentDto>> searchPaymentsByCourseName(String courseName, int page);
    Response<Page<PaymentUserDto>> getPaymentByUser(int page);
}
