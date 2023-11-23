package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserServiceImplTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Test
    void register() {
        User user = new User();
        user.setName("Super Admin");
        user.setEmail("admin@email.com");
        user.setPhone("082136102653");
        user.setPassword(passwordEncoder.encode("admin"));
        user.setCity("Jakarta");
        user.setCountry("Indonesia");
        user.setImageProfile("images/uiux.jpg");
        user.setRole(Role.ADMIN);

        userRepository.save(user);
    }
}