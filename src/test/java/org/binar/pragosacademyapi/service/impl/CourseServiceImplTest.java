package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.entity.UserVerification;
import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.dto.EditCourseDto;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.CourseRepository;
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
import org.springframework.security.core.context.SecurityContextHolder;

import java.time.LocalDateTime;
import java.util.List;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class CourseServiceImplTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private Authentication authentication;
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private CourseServiceImpl courseService;
    @BeforeEach
    void setUp(){
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        Mockito.when(authentication.getName()).thenReturn("Joko@email.com");
//
//        User user = new User(1L, "Joko WIdodo", "Joko@email.com", "08987876767", "joko12345", "Cilacap", "Indonesia", null, Role.USER, true, null);
//        user.setUserVerification(new UserVerification(1L, user, 123, LocalDateTime.now()));
//
//        Mockito.when(userRepository.findByEmail("Joko@email.com")).thenReturn(user);
    }
    @Test
    void listAllCourse(){

    }
   @Test
   void courseDetail(){

   }
   @Test
   void filter(){

   }
   @Test
   void enrollCourse(){

    }
    @Test
    void enrollPaidCourse(){

    }
   @Test
   void search(){

   }
   @Test
    void filterByCategory(){

   }
   @Test
    void getCoursesByUserAll(){

   }
    @Test
    void createCourse(){

    }
    @Test
    void getCoursesByUserAndStatus(){

    }
    @Test
     void setRating(){

     }
    @Test
     void editCourse(){

    }
    @Test
    void deleteCourse(){

    }
}

