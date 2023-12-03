package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
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
            value = "/courses/type",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> filter(@RequestParam String type) {
        return ResponseEntity.ok(courseService.filter(type));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/course/enroll/{code}"
    )
    public ResponseEntity<Response<String>> enrollClass(@PathVariable String code){
        return ResponseEntity.ok(courseService.enrollCourse(code));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/course/enroll-paid/{code}"
    )
    public ResponseEntity<Response<String>> enrollClassPaid(@PathVariable String code, @RequestBody PaymentRequest request){
        return ResponseEntity.ok(courseService.enrollPaidCourse(code, request));
    }

    @PreAuthorize("hasRole('USER')")
    @PutMapping(
            value = "/course/rating/{courseCode}"
    )
    public ResponseEntity<Response<String>> setRating(@PathVariable String courseCode, @RequestParam Integer rating){
        return ResponseEntity.ok(courseService.setRating(courseCode, rating));
    }

    @GetMapping(
            value = "/courses/search"
    )
    public ResponseEntity<Response<List<CourseDto>>> searchCourse(@RequestParam String courseName){
        return ResponseEntity.ok(courseService.search(courseName));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            value = "/courses/user"
    )
    public ResponseEntity<Response<List<CourseDto>>> getCoursesByUserAll() {
        Response<List<CourseDto>> response = courseService.getCoursesByUserAll();

        HttpStatus httpStatus = response.getError() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;

        return new ResponseEntity<>(response, httpStatus);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            value = "/course",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> createCourse(@RequestBody CourseRequest request){
        return ResponseEntity.ok(courseService.createCourse(request));
    }
  
}
