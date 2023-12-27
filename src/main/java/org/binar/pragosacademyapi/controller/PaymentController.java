package org.binar.pragosacademyapi.controller;


import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class PaymentController {
    @Autowired
    private PaymentService paymentService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<Page<PaymentDto>>> getListPayment(@RequestParam(defaultValue = "0") int page) {
        return ResponseEntity.ok(paymentService.getPaymentsByType(page));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<Page<PaymentDto>>> searchPaymentsByCourseName(
            @RequestParam String courseName, @RequestParam(defaultValue = "0") int page) {
        Response<Page<PaymentDto>> response = paymentService.searchPaymentsByCourseName(courseName, page);
        return ResponseEntity.ok(response);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            value = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<Page<PaymentUserDto>>> historyPaymentUser(@RequestParam(defaultValue = "0") int page){
        return ResponseEntity.ok(paymentService.getPaymentByUser(page));
    }

    @PostMapping("/transfer-bank")
    public Response<PaymentDto> createTransferBankPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createTransferBank(paymentRequest);
    }

    @PostMapping("/credit-card")
    public Response<PaymentDto> createCreditCardPayment(@RequestBody PaymentRequest paymentRequest) {
        return paymentService.createCreditCard(paymentRequest);
    }
}
