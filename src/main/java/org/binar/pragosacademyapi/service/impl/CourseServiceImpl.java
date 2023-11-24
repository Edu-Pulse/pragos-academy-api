package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
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
import java.util.stream.Collectors;


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
                .image(course.getCategory().getImage())
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
}


