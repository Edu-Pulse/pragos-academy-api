package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.AdministrationDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.CourseRepository;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.binar.pragosacademyapi.service.AdminService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final CourseRepository courseRepository;
    @Autowired
    public AdminServiceImpl(UserRepository userRepository, CourseRepository courseRepository){
        this.userRepository = userRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<AdministrationDto> getDataAdministration() {
        Response<AdministrationDto> response = new Response<>();
        try {
            Integer totalUser = userRepository.getTotalUser();
            Integer totalCourse = courseRepository.getTotalCourse();
            Integer totalCoursePremium = courseRepository.getTotalCoursePremium(Type.PREMIUM);

            response.setError(false);
            response.setMessage(ResponseUtils.MESSAGE_SUCCESS);
            response.setData(new AdministrationDto(totalUser, totalCourse, totalCoursePremium));
        }catch (Exception e){
            response.setError(true);
            response.setMessage(ResponseUtils.MESSAGE_FAILED);
            response.setData(null);
        }
        return response;
    }
}
