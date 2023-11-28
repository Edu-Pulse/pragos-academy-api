package org.binar.pragosacademyapi.service;

import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.request.UpdateUserRequest;
import org.binar.pragosacademyapi.entity.response.Response;

public interface UserService {

    Response<UserDto> getProfile();
    Response<String> register(RegisterRequest user);
    Response<String> update(UpdateUserRequest updateUser);
    Response<String> update(String password);
    Response<String> generateCodeVerification(String email);
    Response<String> verification(String email, Integer code);
    boolean checkIsEnable(String email);
    String setDoneChapter(Long detailChapterId);

}
