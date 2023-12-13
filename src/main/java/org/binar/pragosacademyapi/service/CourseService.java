package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.dto.EditCourseDto;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    Response<Page<CourseDto>> listAllCourse(int page, int size);
    Response<CourseDetailDto> courseDetail(String courseCode);
    Response<List<CourseDto>>filter(String type);
    Response<String> enrollCourse(String courseCode);
    Response<String> enrollPaidCourse(String courseCode, PaymentRequest paymentRequest);
    Response<List<CourseDto>> search(String courseName);
    Response<List<CourseDto>> filterByCategory(Integer categoryId);
    Response<Page<CourseDto>> getCoursesByUserAll(int page, int size);
    Response<String> createCourse(CourseRequest request);
    Response<List<CourseDto>> getCoursesByUserAndStatus(String status);
    Response<String> setRating(String courseCode, Integer rating);
    Response<CourseDto> editCourse(String courseId, EditCourseDto editedCourseDto);
    Response<String> deleteCourse(String code);
}
