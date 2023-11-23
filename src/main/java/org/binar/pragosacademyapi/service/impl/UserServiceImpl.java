package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;

    @Override
    public Response<UserDto> getProfile() {
        return null;
    }

    @Override
    public Response<String> register(RegisterRequest user) {
        Response<String> response = new Response<>();
        try{
            User newuser = new User();
            newuser.setName(user.getName());
            newuser.setEmail(user.getEmail());
            newuser.setPhone(user.getPhone());
            newuser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            newuser.setCity(user.getCity());
            newuser.setCountry(user.getCountry());
            newuser.setRole(Role.USER);

            userRepository.save(newuser);
            response.setError(true);
            response.setMessage("Success");
            response.setData("Berhasil register");

        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Terjadi kesalahan. Silahkan coba dengan email atau nomor telepon yang berbeda");
        }
        return response;
    }

    @Override
    public Response<String> update(RegisterRequest updateUser) {
        return null;
    }

    @Override
    public Response<String> update(String password) {
        return null;
    }

    private String getEmailUserContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }else {
            return null;
        }
    }
}
