package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.PaymentService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }
    @Override
    public Response<Page<PaymentDto>> getPaymentsByType(int page) {
        Response<Page<PaymentDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, 10);

        try {
            Page<PaymentDto> payments = paymentRepository.findByType(Type.PREMIUM, pageable);

            response.setError(false);
            response.setMessage(ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS);
            response.setData(payments);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
            response.setData(null);
        }

        return response;
    }

    @Override
    public Response<Page<PaymentDto>> searchPaymentsByCourseName(String courseName, int page) {
        Pageable pageable = PageRequest.of(page, 10);
        try {
            Page<PaymentDto> searchResults = paymentRepository.findByCourseName("%"+courseName+"%", pageable);

            return new Response<>(false, ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS, searchResults);
        } catch (Exception e) {
            return new Response<>(true, ResponseUtils.MESSAGE_FAILED, null);
        }
    }

}
