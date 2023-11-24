package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.CourseRepository;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class   CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    private static final Logger logger = LoggerFactory.getLogger(CourseServiceImpl.class);


    @Override
    public Response<List<CourseDto>> listAllCourse() {
        try {
            List<CourseDto> courseDtos = courseRepository.findAll()
                    .stream()
                    .filter(Objects::nonNull)
                    .map(this::mapToDto)
                    .collect(Collectors.toList());

            return new Response<>(false, "success", courseDtos);
        } catch (Exception e) {
            logger.error("Failed to retrieve courses", e);
            return new Response<>(true, "failed", null);
        }
    }

    private CourseDto mapToDto(Course course) {
        return CourseDto.builder()
                .code(course.getCode())
                .image(course.getImage())
                .category(course.getCategory())
                .name(course.getName())
                .description(course.getDescription())
                .lecturer(course.getLecturer())
                .level(course.getLevel())
                .type(course.getType())
                .price(course.getPrice())
                .discount(course.getDiscount())
                .rating(course.getRating())
                .build();
    }
}


