package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.CategoryDto;
import org.binar.pragosacademyapi.entity.response.Response;

import java.util.List;

public interface CategoryService {
    Response<List<CategoryDto>> listCategory();
}
