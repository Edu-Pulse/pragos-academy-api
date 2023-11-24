package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface   CourseService {
    Response<List<CourseDto>> listAllCourse();
    Response<CourseDetailDto> courseDetail(String courseCode);
}
