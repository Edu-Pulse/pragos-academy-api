package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.entity.request.ChapterRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.service.ChapterService;
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

@RequestMapping("/course")
@RestController
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private ChapterService chapterService;

    @GetMapping(
            value = "/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<CourseDetailDto>> courseDetail(@PathVariable String code){
        return ResponseEntity.ok(courseService.courseDetail(code));
    }

    @GetMapping(
            value = "/all",
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
            value = "/type",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> filter(@RequestParam String type) {
        return ResponseEntity.ok(courseService.filter(type));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/enroll/{code}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> enrollClass(@PathVariable String code){
        return ResponseEntity.ok(courseService.enrollCourse(code));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/enroll-paid/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> enrollClassPaid(@PathVariable String code, @RequestBody PaymentRequest request){
        return ResponseEntity.ok(courseService.enrollPaidCourse(code, request));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/rating/{courseCode}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> setRating(@PathVariable String courseCode, @RequestParam Integer rating){
        return ResponseEntity.ok(courseService.setRating(courseCode, rating));
    }

    @GetMapping(
            value = "/search",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> searchCourse(@RequestParam String courseName){
        return ResponseEntity.ok(courseService.search(courseName));
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            value = "/user",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> getCoursesByUserAll() {
        Response<List<CourseDto>> response = courseService.getCoursesByUserAll();

        HttpStatus httpStatus = response.getError() ? HttpStatus.INTERNAL_SERVER_ERROR : HttpStatus.OK;

        return new ResponseEntity<>(response, httpStatus);
    }
    @PreAuthorize("hasRole('USER')")
    @GetMapping(
            value = "/user/status",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> getCoursesByUserAndStatus(@RequestParam String status) {
        return ResponseEntity.ok(courseService.getCoursesByUserAndStatus(status));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            value = "/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> addChapter(@PathVariable String code, @RequestBody ChapterRequest request){
        return ResponseEntity.ok(chapterService.addChapter(code, request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> createCourse(@RequestBody CourseRequest request){
        return ResponseEntity.ok(courseService.createCourse(request));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(
            value = "/edit/{code}",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<CourseDto>> editCourse(@PathVariable String code, @RequestBody CourseDto request) {
        return ResponseEntity.ok(courseService.editCourse(code, request));
    }

}
