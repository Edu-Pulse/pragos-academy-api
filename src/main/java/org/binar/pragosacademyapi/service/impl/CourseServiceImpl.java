package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.Course;
import org.binar.pragosacademyapi.entity.dto.ChapterDto;
import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.dto.DetailChapterDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.CourseRepository;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
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
                List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), detailChapter.getVideo(), detailChapter.getMaterial())).collect(Collectors.toList()))).collect(Collectors.toList());
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
    public Response<List<CourseDto>> filter(Boolean discount, Long category, String level, String type) {
        List<Course> filteredCourses = courseRepository.filter(discount, category, level, type);
        List<CourseDto> filteredCourseDTO = filteredCourses.stream().map(course -> {
            return convertToDto(course);
        }).collect(Collectors.toList());

        Response<List<CourseDto>> responses = new Response<>();
        responses.setData(filteredCourseDTO);
        responses.setMessage("Course Filter Sucsess");
        return responses;

    }

    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto();
        BeanUtils.copyProperties(course, dto);
        dto.setCategory(course.getCategory().getName());
        dto.setLevel(course.getLevel().toString());
        dto.setType(course.getType().toString());
        dto.setImage(course.getCategory().getImage()); // asumsikan category memiliki properti image
        return dto;
    }
}
