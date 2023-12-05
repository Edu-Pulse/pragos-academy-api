package org.binar.pragosacademyapi.controller;


import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @GetMapping("/type/{type}")
    public Response<List<PaymentDto>> getPaymentsByType(@PathVariable String type) {
        return paymentService.getPaymentsByType(type);
    }
}
