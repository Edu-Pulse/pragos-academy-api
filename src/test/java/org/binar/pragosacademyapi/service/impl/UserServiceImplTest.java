package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.entity.UserVerification;
import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;import java.time.LocalDateTime;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private Authentication authentication;
    @InjectMocks
    private UserServiceImpl userServiceImpl;

    @BeforeEach
    void setUp(){
        SecurityContextHolder.getContext().setAuthentication(authentication);
        Mockito.when(authentication.getName()).thenReturn("Joko@email.com");

        User user = new User(1L, "Joko WIdodo", "Joko@email.com", "08987876767", "joko12345", "Cilacap", "Indonesia", null, Role.USER, true, null);
        user.setUserVerification(new UserVerification(1L, user, 123, LocalDateTime.now()));

        Mockito.when(userRepository.findByEmail("Joko@email.com")).thenReturn(user);
    }

    @Test
    void getProfile() {
        Response<UserDto> getProfile = userServiceImpl.getProfile();
        Mockito.verify(userRepository, Mockito.times(1)).findByEmail("Joko@email.com");
        assertNotNull(getProfile);
        assertFalse(getProfile.getError());
        assertEquals("Berhasil", getProfile.getMessage());
        assertNotNull(getProfile.getData());
        assertEquals("Joko WIdodo", getProfile.getData().getName());
        assertEquals("Joko@email.com", getProfile.getData().getEmail());
        assertEquals("08987876767", getProfile.getData().getPhone());
        assertEquals("Cilacap", getProfile.getData().getCity());
        assertEquals("Indonesia", getProfile.getData().getCountry());
    }

    @Test
    void testRegister() {
    }

    @Test
    void update() {
    }

    @Test
    void testUpdate() {
    }

    @Test
    void generateCodeVerification() {
    }

    @Test
    void verification() {
    }

    @Test
    void checkIsEnable() {
    }

    @Test
    void setDoneChapter() {

    }

    @Test
    void resetPassword() {
    }

    @Test
    void forgotPassword() {
    }

    @Test
    void changePassword() {

    }

    @Test
    void getEmailUserContext() {
        String email = userServiceImpl.getEmailUserContext();
        assertNotNull(email);
        assertEquals("Joko@email.com", email);
    }
}