package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;


    @GetMapping
    public Response<List<CourseDto>> listAllCourses() {
        return courseService.listAllCourse();
    }
    @GetMapping(
            value = "/course/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<CourseDetailDto>> courseDetail(@PathVariable String code){
        return ResponseEntity.ok(courseService.courseDetail(code));

    }
}
