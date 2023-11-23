package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;

public interface UserService {

    Response<UserDto> getProfile(Long userId);
    Response<UserDto> register(RegisterRequest user);
    Response<String> update(Long userId, RegisterRequest updateUser);
    Response<String> update(Long userId, String password);

}
