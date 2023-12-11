package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentSearchDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.PaymentService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository){
        this.paymentRepository = paymentRepository;
    }
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
            response.setMessage(ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS);
            response.setData(paymentDtoList);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
            response.setData(null);
        }

        return response;
    }

    @Override
    public Response<List<PaymentSearchDto>> searchPaymentsByCourseName(String courseName) {
        try {
            List<PaymentSearchDto> searchResults = paymentRepository.findByCourseName("%"+courseName+"%")
                    .stream()
                    .map(payment -> {
                        PaymentSearchDto result = new PaymentSearchDto();
                        result.setPaymentId(payment.getId());
                        result.setCourseName(payment.getCourse().getName());
                        result.setAmount(payment.getAmount());
                        return result;
                    })
                    .collect(Collectors.toList());

            return new Response<>(false, ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS, searchResults);
        } catch (Exception e) {
            return new Response<>(true, ResponseUtils.MESSAGE_FAILED, null);
        }
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
