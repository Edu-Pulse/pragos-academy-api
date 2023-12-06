package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private PaymentRepository paymentRepository;
    @Override
    public Response<List<PaymentDto>> getPaymentsByType() {
        Response<List<PaymentDto>> response = new Response<>();

        try {
            List<Payment> payments = paymentRepository.findByType(Type.PREMIUM);
            List<PaymentDto> paymentDtoList = new ArrayList<>();

            for (Payment payment : payments) {
                PaymentDto paymentDto = createPaymentDto(payment);
                paymentDtoList.add(paymentDto);
            }

            response.setError(false);
            response.setMessage("Success to get payments by type");
            response.setData(paymentDtoList);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to get payments by type");
            response.setData(null);
        }

        return response;
    }

    private PaymentDto createPaymentDto(Payment payment) {
        PaymentDto paymentDto = new PaymentDto();
        paymentDto.setUserId(payment.getUser().getId());
        paymentDto.setCategoryName(payment.getCourse().getCategory().getName());
        paymentDto.setCourseName(payment.getCourse().getName());
        paymentDto.setStatus(payment.getStatus());
        paymentDto.setPaymentMethod(payment.getPaymentMethod());
        paymentDto.setPaymentDate(payment.getPaymentDate());

        return paymentDto;
    }
}
