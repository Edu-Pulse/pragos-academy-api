package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.CategoryDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.CategoryRepository;
import org.binar.pragosacademyapi.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    @Override
    public Response<List<CategoryDto>> listCategory() {
        Response<List<CategoryDto>> response = new Response<>();
        try {
            List<CategoryDto> categories = categoryRepository.selectAll().stream().map(category -> new CategoryDto(category.getId(), category.getName(), category.getImage())).collect(Collectors.toList());
            response.setError(false);
            response.setMessage("success");
            response.setData(categories);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }
        return response;
    }
}
