package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.PaymentDto;
import org.binar.pragosacademyapi.entity.dto.PaymentUserDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class PaymentServiceImplTest {
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserServiceImpl userService;
    @InjectMocks
    private PaymentServiceImpl paymentService;
    @BeforeEach
    void setUp(){
        paymentService = new PaymentServiceImpl(paymentRepository, userService);
    }

    @Test
    void testGetPaymentsByType_success() {
        Page<PaymentDto> expectedPayments = createMockedPayments(); //expectednya
        when(paymentRepository.findByType(Type.PREMIUM, PageRequest.of(0, 10)))
                .thenReturn(expectedPayments);
        Response<Page<PaymentDto>> response = paymentService.getPaymentsByType(0); // actualnya

        assertEquals(false, response.getError());
        assertEquals(ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS, response.getMessage());
        assertEquals(expectedPayments, response.getData());
    }
    @Test
    public void testGetPaymentsByType_failed() {
        when(paymentRepository.findByType(Type.PREMIUM, PageRequest.of(0, 10)))
                .thenThrow(new RuntimeException("Error fetching payments"));
        Response<Page<PaymentDto>> response = paymentService.getPaymentsByType(0);

        assertTrue(response.getError());
        assertNull(response.getData());
        assertEquals("Failed. An exception occurred", response.getMessage());
    }

    @Test
    public void testSearchPaymentsByCourseName_success() {
        String courseName = "Java";
        int page = 0;
        Page<PaymentDto> expectedPayments = createMockedPayments(); // expected
        when(paymentRepository.findByCourseName("%Java%", PageRequest.of(0, 10)))
                .thenReturn(expectedPayments);
        Response<Page<PaymentDto>> response = paymentService.searchPaymentsByCourseName(courseName, page); // actual

        assertFalse(response.getError());
        assertEquals(ResponseUtils.MESSAGE_SUCCESS_GET_DATA_PAYMENTS, response.getMessage());
        assertEquals(expectedPayments, response.getData());
    }

    @Test
    public void testSearchPaymentsByCourseName_failed() {
        String courseName = "Python";
        int page = 1;
        when(paymentRepository.findByCourseName("%Python%", PageRequest.of(1, 10)))
                .thenThrow(new RuntimeException("Error searching payments"));
        Response<Page<PaymentDto>> response = paymentService.searchPaymentsByCourseName(courseName, page);

        assertTrue(response.getError());
        assertEquals(ResponseUtils.MESSAGE_FAILED, response.getMessage());
        assertNull(response.getData());
    }

    private Page<PaymentDto> createMockedPayments() {
        List<PaymentDto> payments = new ArrayList<>();
        payments.add(new PaymentDto(1L, "Programming", "Java Fundamental", true, "Bank Transfer", LocalDateTime.now()));
        payments.add(new PaymentDto(2L, "Design", "UI/UX Design Course", false, "Credit Card", LocalDateTime.now().minusDays(2)));
        payments.add(new PaymentDto(3L, "Data Science", "Machine Learning A-Z", true, "GoPay", LocalDateTime.now().minusDays(4)));
        return new PageImpl<>(payments, PageRequest.of(0, 10), payments.size());
    }

    @Test
    void testGetPaymentByUserSuccess() {
        int page = 1;
        String userEmail = "test@example.com";
        Page<PaymentUserDto> expectedPage = createMockedPaymentsByUser();
        when(userService.getEmailUserContext()).thenReturn(userEmail);
        when(paymentRepository.paymentByUser(userEmail, PageRequest.of(page, 2))).thenReturn(expectedPage);
        Response<Page<PaymentUserDto>> response = paymentService.getPaymentByUser(page);

        assertFalse(response.getError());
        assertEquals(ResponseUtils.MESSAGE_SUCCESS, response.getMessage());
        assertEquals(expectedPage, response.getData());
    }

    @Test
    void testGetPaymentByUser_failed() {
        int page = 1;
        when(userService.getEmailUserContext()).thenThrow(new RuntimeException("Test exception"));
        Response<Page<PaymentUserDto>> response = paymentService.getPaymentByUser(page);

        assertTrue(response.getError());
        assertEquals(ResponseUtils.MESSAGE_FAILED, response.getMessage());
        assertNull(response.getData());
    }
    private Page<PaymentUserDto> createMockedPaymentsByUser() {
        List<PaymentUserDto> payments = new ArrayList<>();
        PaymentUserDto payment1 = new PaymentUserDto(
                "https://example.com/image1.jpg",
                "Programming",
                "Java Fundamental",
                "John Doe",
                Level.BEGINNER,
                true,
                "Bank transfer",
                100000L,
                LocalDateTime.now()
        );
        PaymentUserDto payment2 = new PaymentUserDto(
                "https://example.com/image2.jpg",
                "Design",
                "UI/UX Design Course",
                "Jane Smith",
                Level.INTERMEDIATE,
                false,
                "Credit Card",
                200000L,
                LocalDateTime.now().minusDays(2)
        );
        payments.add(payment1);
        payments.add(payment2);
        return new PageImpl<>(payments, PageRequest.of(0, 2), payments.size());
    }
}
