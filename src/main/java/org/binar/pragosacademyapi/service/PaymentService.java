package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentSearchDto;
import org.binar.pragosacademyapi.entity.response.Response;

import java.util.List;

public interface PaymentService {
    Response<List<PaymentDto>> getPaymentsByType();
    Response<List<PaymentSearchDto>> searchPaymentsByCourseName(String courseName);
}
