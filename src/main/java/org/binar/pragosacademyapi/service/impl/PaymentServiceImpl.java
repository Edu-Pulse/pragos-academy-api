package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.PaymentService;
import org.binar.pragosacademyapi.service.UserService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final PaymentRepository paymentRepository;
    private final UserServiceImpl userService;
    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository, UserServiceImpl userService){
        this.paymentRepository = paymentRepository;
        this.userService = userService;
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

    @Override
    public Response<Page<PaymentUserDto>> getPaymentByUser(int page) {
        Response<Page<PaymentUserDto>> response = new Response<>();
        Pageable pageable = PageRequest.of(page, 10);
        try {
            Page<PaymentUserDto> paymentUserDto = paymentRepository.paymentByUser(userService.getEmailUserContext(), pageable);
            response.setError(false);
            response.setMessage(ResponseUtils.MESSAGE_SUCCESS);
            response.setData(paymentUserDto);
        }catch (Exception e){
            response.setError(true);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
        }
        return response;
    }

}
