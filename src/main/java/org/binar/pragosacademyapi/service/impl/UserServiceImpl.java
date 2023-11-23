package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response<UserDto> getProfile(Long userId) {
        return null;
    }

    @Override
    public Response<UserDto> register(RegisterRequest user) {
        return null;
    }

    @Override
    public Response<String> update(Long userId, RegisterRequest updateUser) {
        return null;
    }

    @Override
    public Response<String> update(Long userId, String password) {
        return null;
    }
}
