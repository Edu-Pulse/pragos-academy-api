package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.PaymentGatewayService;
import org.binar.pragosacademyapi.service.PaymentService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {
    private String transactionId;
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private PaymentGatewayService paymentGatewayService;
    @Autowired
    public PaymentServiceImpl(PaymentRequest atmPaymentGateway, PaymentRepository paymentRepository, UserServiceImpl userService){
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
        Pageable pageable = PageRequest.of(page, 2);
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

    @Override
    public Response<PaymentDto> processPayment(PaymentRequest paymentRequest) {
        try {
            validatePaymentRequest(paymentRequest);
            Payment payment = convertToPaymentEntity(paymentRequest);
            transactionId = paymentGatewayService.initiatePayment(payment);
            payment.setTransactionId(transactionId);
            Payment savedPayment = paymentRepository.save(payment);
            PaymentDto responseDto = convertToPaymentDto(savedPayment);
            return new Response<>(false, "Payment processed successfully", responseDto);
        } catch (Exception e) {
            return new Response<>(true, "Payment processing failed: " + e.getMessage(), null);
        }
    }

    @Override
    public Response<String> initiatePaymentGateaway(PaymentRequest paymentRequest) {
        try {
            validatePaymentRequest(paymentRequest);
            Payment payment = convertToPaymentEntity(paymentRequest);
            transactionId = paymentGatewayService.initiatePayment(payment);
            payment.setTransactionId(transactionId);
            paymentRepository.save(payment);
            return new Response<>(false, "Payment initiation successful", transactionId);
        } catch (Exception e) {
            return new Response<>(true, "Payment initiation failed: " + e.getMessage(), null);
        }
    }

    @Override
    public Response<String> handlePaymentCallback(String transactionId, String status) {
        try {
            updatePaymentStatus(transactionId, status);
            return new Response<>(false, "Payment callback processed successfully", "Callback processed");
        } catch (Exception e) {
            return new Response<>(true, "Payment callback processing failed: " + e.getMessage(), null);
        }
    }

    private void updatePaymentStatus(String transactionId, String status) {
        Payment payment = paymentRepository.findByTransactionId(transactionId);
        if (payment != null){
            payment.setStatus(Boolean.valueOf(status));
            paymentRepository.save(payment);
        }else {
            throw new RuntimeException("Payment with transactionId" + transactionId + "not found.");
        }
    }

    private PaymentDto convertToPaymentDto(Payment savedPayment) {
        PaymentDto paymentDto = new PaymentDto();
        BeanUtils.copyProperties(savedPayment, paymentDto);
        return paymentDto;
    }

    private Payment convertToPaymentEntity(PaymentRequest paymentRequest) {
        Payment payment = new Payment();
        BeanUtils.copyProperties(paymentRequest, payment);
        return payment;
    }

    private void validatePaymentRequest(PaymentRequest paymentRequest) {
        if (paymentRequest.getAmount() == null || paymentRequest.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be a positive value.");
        }

        if (paymentRequest.getCardNumber() == null || paymentRequest.getCardNumber().isEmpty()) {
            throw new IllegalArgumentException("Card number is required.");
        }
    }
}
