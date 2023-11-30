package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.CategoryDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.CategoryService;
import org.binar.pragosacademyapi.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CategoryController {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private CourseService courseService;

    @GetMapping(
            value = "/categories",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CategoryDto>>> listCategory(){
        return ResponseEntity.ok(categoryService.listCategory());
    }
    @GetMapping(
            value = "/category/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<List<CourseDto>>> courseByCategory(@PathVariable Integer id){
        return ResponseEntity.ok(courseService.filterByCategory(id));
    }
}
