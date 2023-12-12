package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.CategoryDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.CategoryRepository;
import org.binar.pragosacademyapi.service.CategoryService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository){
        this.categoryRepository = categoryRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<CategoryDto>> listCategory() {
        Response<List<CategoryDto>> response = new Response<>();
        try {
            response.setError(false);
            response.setMessage(ResponseUtils.MESSAGE_SUCCESS);
            response.setData(categoryRepository.selectAll());
        }catch (Exception e){
            response.setError(true);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
            response.setData(null);
        }
        return response;
    }
}
