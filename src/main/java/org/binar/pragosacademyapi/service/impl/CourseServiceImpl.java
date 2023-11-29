package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.Payment;
import org.binar.pragosacademyapi.entity.dto.ChapterDto;
import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.CourseRepository;
import org.binar.pragosacademyapi.repository.PaymentRepository;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public Response<List<CourseDto>> listAllCourse() {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<CourseDto> courseDtos = courseRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setError(false);
            response.setMessage("Success to get all courses");
            response.setData(courseDtos);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to get all courses");
            response.setData(null);
        }
        return response;
    }


    @Override
    public Response<CourseDetailDto> courseDetail(String courseCode) {
        Response<CourseDetailDto> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(courseCode);
            if (course != null){
                List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getName(), chapter.getVideo(), chapter.getMateri(), chapter.getCapther())).collect(Collectors.toList());
                response.setError(false);
                response.setMessage("Success to get data "+ courseCode);
                response.setData(
                        new CourseDetailDto(course.getCode(), course.getCategory().getImage(),course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), course.getRating(), chapters)
                );
            }else {
                response.setError(true);
                response.setMessage("Failed to get data "+ courseCode+ " not found");
                response.setData(null);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed to get data "+ courseCode);
            response.setData(null);
        }
        return response;
    }

    @Override
    @Transactional
    public Response enrollCourse(String courseCode, PaymentRequest request) {
        try {
            Course course = courseRepository.findByCode(courseCode);
            if (course != null) {
                Payment payment = new Payment();
                payment.setCourse(course);
                payment.setStatus(false); // Pembayaran status false
                payment.setAmount(Long.valueOf(course.getPrice()));
                payment.setPaymentDate(LocalDateTime.now()); // Atur waktu pembayaran ke waktu saat ini

                // Set informasi pembayaran dari PaymentRequest
                payment.setCardNumber(request.getCardNumber());
                payment.setCardHolderName(request.getCardHolderName());
                payment.setCvv(request.getCvv());
                payment.setExpiryDate(request.getExpiryDate());

                paymentRepository.save(payment);
                return new Response(false, "success", null);
            } else {
                return new Response(true, "failed", null);
            }
        } catch (Exception e) {
            return new Response(true, "failed", null);
        }
    }



    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto();
        BeanUtils.copyProperties(course, dto);
        dto.setImage(course.getCategory().getImage()); // asumsikan category memiliki properti image
        return dto;
    }
}
