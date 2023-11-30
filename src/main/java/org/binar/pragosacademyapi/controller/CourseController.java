package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;

    @GetMapping(
            value = "/course/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<CourseDetailDto>> courseDetail(@PathVariable String code){
        return ResponseEntity.ok(courseService.courseDetail(code));
    }

    @GetMapping(
            value = "/courses",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> listAllCourses() {
        try {
            Response<List<CourseDto>> response = courseService.listAllCourse();
            if (response.getError()) {
                return ResponseEntity.status(500).body(response); // 500 Internal Server Error
            } else {
                return ResponseEntity.ok(response);
            }
        } catch (Exception e) {
            Response<List<CourseDto>> response = new Response<>();
            response.setError(true);
            response.setMessage("Internal Server Error");
            response.setData(null);
            return ResponseEntity.status(500).body(response); // 500 Internal Server Error
        }

    }
    @GetMapping(
            value = "/courses/{type}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> filter(@PathVariable String type) {
        return ResponseEntity.ok(courseService.filter(type));
    }
    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/course/enroll/{code}"
    )
    public ResponseEntity<Response<String>> enrollClass(@PathVariable String code){
        return ResponseEntity.ok(courseService.enrollCourse(code));
    }

    @GetMapping(
            value = "/courses/search"
    )
    public ResponseEntity<Response<List<CourseDto>>> searchCourse(@RequestParam String courseName){
        return ResponseEntity.ok(courseService.search(courseName));
    }
}
