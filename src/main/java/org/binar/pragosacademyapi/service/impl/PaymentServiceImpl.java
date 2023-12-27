package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.PaymentMethod;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.NotificationService;
import org.binar.pragosacademyapi.service.PaymentService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PaymentServiceImpl implements PaymentService {
    @Autowired
    private final PaymentRepository paymentRepository;
    @Autowired
    private final UserServiceImpl userService;
    @Autowired
    private NotificationService notificationService;
    boolean sendEmailNotification = true;

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
    public Response<PaymentDto> createTransferBank(PaymentRequest paymentRequest) {
        try {
            if (!paymentRequest.getPaymentMethod().equals(PaymentMethod.TRANSFER_BANK)){
                throw new IllegalArgumentException("Metode pembayaran harus transfer bank");
            }
            if (paymentRequest.getAmount() <= 0){
                throw new IllegalArgumentException("Jumlah pembayaran harus lebih besar dari O");
            }
            PaymentDto paymentDto = new PaymentDto(
                    paymentRequest.getPaymentMethod(),
                    paymentRequest.getAmount(),
                    paymentRequest.getUserId(),
                    paymentRequest.getCourseCode()
            );
            Payment payment = new Payment();
            payment.setPaymentMethod(paymentDto.getPaymentMethod());
            payment.setAmount(paymentDto.getAmount());
            payment.setUser(paymentDto.getUserId());
            payment.setCourse(paymentDto.getCourseCode());
            paymentRepository.save(payment);

            if (sendEmailNotification) {
                String email = paymentRequest.getUserId().getEmail();
                String message = "Pembayaran Anda telah berhasil dilakukan.\n\nBerikut adalah detail pembayaran Anda:\n\n* Metode pembayaran: [Metode pembayaran]\n* Jumlah pembayaran: [Jumlah pembayaran]\n* Kursus: [Kode kursus]";
                notificationService.sendNotification(email, message);
            }
            return Response.<PaymentDto>builder()
                    .error(false)
                    .message("Pembayaran transfer bank berhasil.")
                    .data(paymentDto)
                    .build();
        } catch (Exception e) {
            return Response.<PaymentDto>builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
    }

    @Override
    public Response<PaymentDto> createCreditCard(PaymentRequest paymentRequest) {
        try {
            if (!paymentRequest.getPaymentMethod().equals(PaymentMethod.CREDIT_CARD)) {
                throw new IllegalArgumentException("Metode pembayaran harus credit card");
            }
            if (paymentRequest.getAmount() <= 0) {
                throw new IllegalArgumentException("Jumlah pembayaran harus lebih besar dari 0");
            }
            validateCreditCardData(paymentRequest);

            boolean otorisasiBerhasil = authorizeCreditCard(paymentRequest);
            if (!otorisasiBerhasil) {
                throw new IllegalArgumentException("Otorisasi pembayaran gagal");
            }

            PaymentDto paymentDto = new PaymentDto(
                    paymentRequest.getPaymentMethod(),
                    paymentRequest.getAmount(),
                    paymentRequest.getUserId(),
                    paymentRequest.getCourseCode(),
                    paymentRequest.getCardNumber(),
                    paymentRequest.getCardHolderName(),
                    paymentRequest.getCvv(),
                    paymentRequest.getExpiryDate()
            );
            Payment payment = new Payment();
            payment.setPaymentMethod(paymentDto.getPaymentMethod());
            payment.setAmount(paymentDto.getAmount());
            payment.setUser(paymentDto.getUserId());
            payment.setCourse(paymentDto.getCourseCode());
            paymentRepository.save(payment);

            if (sendEmailNotification) {
                String email = paymentRequest.getUserId().getEmail();
                String message = "Pembayaran Anda telah berhasil dilakukan.\n\nBerikut adalah detail pembayaran Anda:\n\n* Metode pembayaran: [Metode pembayaran]\n* Jumlah pembayaran: [Jumlah pembayaran]\n* Kursus: [Kode kursus]";
                notificationService.sendNotification(email, message);
            }
            return Response.<PaymentDto>builder()
                    .error(false)
                    .message("Pembayaran credit card berhasil.")
                    .data(paymentDto)
                    .build();
        } catch (Exception e) {
            return Response.<PaymentDto>builder()
                    .error(true)
                    .message(e.getMessage())
                    .build();
        }
    }
    private void validateCreditCardData(PaymentRequest paymentRequest) {
        String cardNumber = paymentRequest.getCardNumber();
        String expiryDate = String.valueOf(paymentRequest.getExpiryDate());
        String cvv = paymentRequest.getCvv();

        // Validasi nomor kartu kredit
        if (!isValidCardNumber(cardNumber)) {
            throw new IllegalStateException("Nomor kartu kredit tidak valid");
        }

        // Validasi tanggal kadaluarsa kartu kredit
        if (!isValidExpiryDate(expiryDate)) {
            throw new IllegalStateException("Tanggal kadaluarsa kartu kredit tidak valid");
        }

        // Validasi CVV kartu kredit
        if (!isValidCvv(cvv)) {
            throw new IllegalStateException("CVV tidak valid");
        }
    }

    private boolean isValidCardNumber(String cardNumber) {
        //regular expression untuk memvalidasi nomor kartu kredit
        String regex = "^(?:4[0-9]{12}(?:[0-9]{3})?|5[1-5][0-9]{14}|6(?:011|5[0-9][0-9])[0-9]{12}|3[47][0-9]{13}|3(?:0[0-5]|[68][0-9])[0-9]{11}|(?:2131|1800|35\\d{3})\\d{11})$";
        return cardNumber.matches(regex);
    }
    private boolean isValidExpiryDate(String expiryDate) {
        //format MM/YY
        String regex = "^(0[1-9]|1[0-2])/[0-9]{2}$";
        if (!expiryDate.matches(regex)) {
            return false;
        }
        // Periksa tanggal kadaluarsa
        int month = Integer.parseInt(expiryDate.substring(0, 2));
        int year = Integer.parseInt(expiryDate.substring(3)) + 2000;
        LocalDate currentDate = LocalDate.now();
        LocalDate expiryLocalDate = LocalDate.of(year, month, 1);
        return expiryLocalDate.isAfter(currentDate);
    }
    private boolean isValidCvv(String cvv) {
        //3 atau 4 digit angka
        return cvv.matches("^[0-9]{3,4}$");
    }

    private boolean authorizeCreditCard(PaymentRequest paymentRequest) {
        //harusnya implementasi api pake midtrans cuman bingung trial nya gmna
        String cardNumber = paymentRequest.getCardNumber();
        String expiryDate = String.valueOf(paymentRequest.getExpiryDate());
        String cvv = paymentRequest.getCvv();

        boolean authorizationSuccessful = !cardNumber.isEmpty() && !expiryDate.isEmpty() && !cvv.isEmpty();
        return authorizationSuccessful;
    }
}
