package org.binar.pragosacademyapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.*;
import org.binar.pragosacademyapi.entity.dto.*;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.CourseStatus;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.*;
import org.binar.pragosacademyapi.service.CourseService;
import org.binar.pragosacademyapi.service.NotificationService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final PaymentRepository paymentRepository;
    private final UserServiceImpl userService;
    private final UserDetailChapterRepository userDetailChapterRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final NotificationService notificationService;
    private static final String SUCCESS = ResponseUtils.MESSAGE_SUCCESS;
    private static final String FAILED = ResponseUtils.MESSAGE_FAILED;
    private static final String SUCCESS_GET_DATA_COURSE = ResponseUtils.MESSAGE_SUCCESS_GET_DATA_COURSE;
    private static final String FAILED_GET_DATA_COURSE = ResponseUtils.MESSAGE_FAILED_GET_DATA_COURSE;
    private static final String SUCCESS_ENROLL_COURSE = ResponseUtils.MESSAGE_SUCCESS_ENROLL_COURSE;
    private static final String FAILED_ENROLL_COURSE = ResponseUtils.MESSAGE_FAILED_ENROLL_COURSE;

    @Autowired
    public CourseServiceImpl(CourseRepository courseRepository,
                             PaymentRepository paymentRepository,
                             UserServiceImpl userService,
                             UserDetailChapterRepository userDetailChapterRepository,
                             UserRepository userRepository, CategoryRepository categoryRepository,
                             NotificationService notificationService){
        this.courseRepository = courseRepository;
        this.paymentRepository = paymentRepository;
        this.userService = userService;
        this.userDetailChapterRepository = userDetailChapterRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.notificationService = notificationService;
    }

    @Override
    public Response<Page<CourseDto>> listAllCourse(int page, int size) {
        Response<Page<CourseDto>> response = new Response<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Course> coursePage = courseRepository.findAll(pageable);
            List<CourseDto> courseDtoList = coursePage.getContent().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtoList, pageable,coursePage.getTotalElements());

            response.setError(false);
            response.setMessage(String.format(SUCCESS_GET_DATA_COURSE + "courses - Page %d of %d", courseDtoPage.getNumber(), courseDtoPage.getTotalPages()));
            response.setData(courseDtoPage);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }


    @Override
    public Response<CourseDetailDto> courseDetail(String courseCode) {
        Response<CourseDetailDto> response = new Response<>();
        User user = userRepository.findByEmail(userService.getEmailUserContext()).orElse(null);
        try {
            if (user != null) {
                Course course = paymentRepository.detailCourse(courseCode, user.getEmail());
                if (course != null) {
                    List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), detailChapter.getVideo(), detailChapter.getMaterial(), userDetailChapterRepository.existsByUserIdAndDetailChapter_IdAndAndIsDone(user.getId(), detailChapter.getId(), true))).collect(Collectors.toList()))).collect(Collectors.toList());
                    response.setError(false);
                    response.setMessage(SUCCESS_GET_DATA_COURSE + courseCode);
                    response.setData(
                            new CourseDetailDto(course.getCode(), course.getCategory().getImage(), course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()), courseRepository.getCountDetailChapter(courseCode), courseRepository.getCountDetailChapterDone(courseCode, user.getEmail()), chapters)
                    );
                } else {
                    course = courseRepository.findById(courseCode).orElse(null);
                    if (course != null) {
                        List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), null, null, null)).collect(Collectors.toList()))).collect(Collectors.toList());
                        response.setError(false);
                        response.setMessage(SUCCESS_GET_DATA_COURSE + courseCode);
                        response.setData(
                                new CourseDetailDto(course.getCode(), course.getCategory().getImage(), course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()), courseRepository.getCountDetailChapter(courseCode), null, chapters)
                        );
                    } else {
                        response.setError(true);
                        log.error(userService.getEmailUserContext());
                        response.setMessage( FAILED_GET_DATA_COURSE + courseCode + ResponseUtils.DATA_NOT_FOUND);
                        response.setData(null);
                    }
                }
            } else {
                Course course = courseRepository.findById(courseCode).orElse(null);
                if (course != null) {
                    List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), null, null, null)).collect(Collectors.toList()))).collect(Collectors.toList());
                    response.setError(false);
                    response.setMessage(SUCCESS_GET_DATA_COURSE + courseCode);
                    response.setData(
                            new CourseDetailDto(course.getCode(), course.getCategory().getImage(), course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()), courseRepository.getCountDetailChapter(courseCode), null, chapters)
                    );
                } else {
                    response.setError(true);
                    log.error(userService.getEmailUserContext());
                    response.setMessage(FAILED_GET_DATA_COURSE+ courseCode + ResponseUtils.DATA_NOT_FOUND);
                    response.setData(null);
                }
            }
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> enrollCourse(String courseCode) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(courseCode);
            User user = userRepository.findByEmail(userService.getEmailUserContext()).orElse(null);
            if (user != null){
                if (course != null) {
                    Payment checkPayment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
                    if (checkPayment == null) {
                        if (course.getPrice() == 0) {
                            Payment payment = new Payment();
                            payment.setUser(user);
                            payment.setCourse(course);
                            payment.setAmount(0L);
                            payment.setPaymentMethod("GRATIS");
                            payment.setCardNumber("-");
                            payment.setCardHolderName("-");
                            payment.setCvv("-");
                            payment.setExpiryDate("-");
                            payment.setStatus(true);
                            payment.setPaymentDate(LocalDateTime.now());
                            paymentRepository.save(payment);

                            response.setError(false);
                            response.setMessage(SUCCESS_ENROLL_COURSE + courseCode);
                            notificationService.sendNotification(user.getId(), "Kamu telah terdaftar ke kelas " + course.getName() + " Semoga ilmu yang akan dipelajari dapat bermanfaat didunia maupun akhirat");
                        } else {
                            response.setError(true);
                            response.setMessage(FAILED_ENROLL_COURSE + courseCode);
                        }
                    } else {
                        response.setError(true);
                        response.setMessage(FAILED_ENROLL_COURSE + courseCode + " This course is paid");
                    }
                } else {
                    response.setError(true);
                    response.setMessage(FAILED_GET_DATA_COURSE+ courseCode + ". Data not found");
                }
            }else {
                response.setError(true);
                response.setMessage(FAILED);
            }
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
        }
        return response;
    }

    @Override
    public Response<String> enrollPaidCourse(String courseCode, PaymentRequest paymentRequest) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(courseCode);
            User user = userRepository.findByEmail(userService.getEmailUserContext()).orElse(null);
            if (user != null){
                if (course != null) {
                    Payment checkPayment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
                    if (checkPayment == null) {
                        log.info(paymentRequest.getCardNumber());
                        if (paymentRequest.getCardNumber().length() == 16) {
                            Payment payment = new Payment();
                            payment.setUser(user);
                            payment.setCourse(course);
                            double discount = (course.getPrice() * ((double) course.getDiscount() / 100));
                            log.info("Diskon :" + discount);
                            Long amount = (long) (course.getPrice() - discount);
                            payment.setAmount(amount);
                            payment.setPaymentMethod("CREDIT_CARD");
                            payment.setCardNumber(paymentRequest.getCardNumber());
                            payment.setCardHolderName(paymentRequest.getCardHolderName());
                            payment.setCvv(paymentRequest.getCvv());
                            payment.setExpiryDate(paymentRequest.getExpiryDate());
                            payment.setStatus(true);
                            payment.setPaymentDate(LocalDateTime.now());
                            paymentRepository.save(payment);

                            response.setError(false);
                            response.setMessage(SUCCESS_ENROLL_COURSE + courseCode);

                            notificationService.sendNotification(user.getId(), "Kamu telah terdaftar ke kelas " + course.getName() + " Semoga ilmu yang akan dipelajari dapat bermanfaat didunia maupun akhirat");
                        } else {
                            response.setError(true);
                            response.setMessage(FAILED_ENROLL_COURSE + courseCode + ". Invalid card details");
                        }
                    } else {
                        response.setError(true);
                        response.setMessage(FAILED_ENROLL_COURSE + courseCode + ". You have registered for this course");
                    }
                } else {
                    response.setError(true);
                    response.setMessage(FAILED_GET_DATA_COURSE+ courseCode + ". Data not found");
                }
            }else {
                response.setError(true);
                response.setMessage(FAILED);
            }

        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<CourseDto>> search(String courseName) {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<CourseDto> courseDtoList = courseRepository.searchByCourseName("%" + courseName + "%");
            courseDtoList.forEach(courseDto -> courseDto.setRating(getRating(courseDto.getCode())));
            response.setError(false);
            response.setMessage(SUCCESS);
            response.setData(courseDtoList);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<CourseDto>> filterByCategory(Integer categoryId) {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<CourseDto> courseDtoList = courseRepository.searchByCategory(categoryId);
            courseDtoList.forEach(courseDto -> courseDto.setRating(getRating(courseDto.getCode())));
            response.setError(false);
            response.setMessage(SUCCESS);
            response.setData(courseDtoList);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<Page<CourseDto>> getCoursesByUserAll(int page, int size) {
        Response<Page<CourseDto>> response = new Response<>();
        try {
            Pageable pageable = PageRequest.of(page, size);
            List<Payment> payments = paymentRepository.paymentByUserAndStatus(userService.getEmailUserContext());

            List<CourseDto> courseDtos = payments.stream()
                    .map(payment -> convertToDto(payment.getCourse()))
                    .collect(Collectors.toList());
            Page<CourseDto> courseDtoPage = new PageImpl<>(courseDtos, pageable, courseDtos.size());

            response.setError(false);
            response.setMessage("Success to get courses by user");
            response.setData(courseDtoPage);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> createCourse(CourseRequest request) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(request.getCourseCode());
            if (course == null) {
                Course newCourse = new Course();
                newCourse.setCode(request.getCourseCode());
                newCourse.setName(request.getCourseName());
                newCourse.setDescription(request.getDescription());
                newCourse.setLecturer(request.getLecturer());
                newCourse.setIntended(request.getIntended());
                newCourse.setLevel(Level.valueOf(request.getLevel().toUpperCase()));
                newCourse.setType(Type.valueOf(request.getType().toUpperCase()));
                Category category = categoryRepository.findById(request.getCategory()).orElse(null);
                newCourse.setCategory(category);
                newCourse.setPrice(request.getPrice());
                newCourse.setDiscount(request.getDiscount());
                newCourse.setChapters(new ArrayList<>());

                courseRepository.save(newCourse);
                response.setError(false);
                response.setMessage(ResponseUtils.MESSAGE_SUCCESS_ADD_COURSE);
            } else {
                response.setError(true);
                response.setMessage(ResponseUtils.MESSAGE_FAILED_ADD_COURSE + "Course code already exist");
            }
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
        }
        return response;
    }
    @Override
    public Response<List<CourseDto>> getCoursesByUserAndStatus(String status) {
        Response<List<CourseDto>> response = new Response<>();
        try {
            String email = userService.getEmailUserContext();
            List<Payment> payments = paymentRepository.paymentByUserAndStatus(email);
            List<Payment> filteredPayments = payments.stream()
                    .filter(payment -> isCourseInStatus(payment.getCourse(), CourseStatus.valueOf(status.toUpperCase()), email))
                    .collect(Collectors.toList());
            List<CourseDto> courseDtos = filteredPayments.stream()
                    .map(payment -> convertToDto(payment.getCourse()))
                    .collect(Collectors.toList());

            response.setError(false);
            response.setMessage(SUCCESS);
            response.setData(courseDtos);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }

        return response;
    }

    private boolean isCourseInStatus(Course course, CourseStatus status, String userEmail) {
        int countDetailChapterDone = courseRepository.getCountDetailChapterDone(course.getCode(), userEmail);
        int countTotalDetailChapter = courseRepository.getCountDetailChapter(course.getCode());

        if (status == CourseStatus.IN_PROGRESS) {
            return countDetailChapterDone < countTotalDetailChapter;
        } else if (status == CourseStatus.COMPLETED) {
            return countDetailChapterDone == countTotalDetailChapter;
        }
        return false;
    }

    public Response<String> setRating(String courseCode, Integer rating) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(userService.getEmailUserContext()).orElse(null);
            if (user != null){
                Payment payment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
                if (payment != null) {
                    payment.setRating(rating);
                    paymentRepository.save(payment);
                    response.setError(false);
                    response.setMessage(SUCCESS + " set rating to this Course");
                    notificationService.sendNotification(user.getId(), "Terimakasih telah memberikan rating ke course " + payment.getCourse().getName());
                } else {
                    response.setError(true);
                    response.setMessage("Failed set rating to this Course");
                }
            }else {
                response.setError(true);
                response.setMessage(FAILED);
            }
        } catch (Exception e) {
            response.setError(true);
            response.setMessage(FAILED);
        }
        return response;
    }

    @Override
    public Response<CourseDto> editCourse(String courseId, EditCourseDto editedCourseDto) {
        Response<CourseDto> response = new Response<>();
        try {
            // Step 1: Retrieve the existing course
            Optional<Course> optionalCourse = courseRepository.findById(courseId);

            if (optionalCourse.isPresent()) {
                Course existingCourse = optionalCourse.get();

                // Step 2: Update the existing course with the information from editedCourseDto
                existingCourse.setName(editedCourseDto.getName());
                existingCourse.setDescription(editedCourseDto.getDescription());
                existingCourse.setIntended(editedCourseDto.getIntended());
                existingCourse.setLecturer(editedCourseDto.getLecturer());
                existingCourse.setLevel(Level.valueOf(editedCourseDto.getLevel().toUpperCase()));
                existingCourse.setType(Type.valueOf(editedCourseDto.getType().toUpperCase()));
                existingCourse.setPrice(editedCourseDto.getPrice());
                existingCourse.setDiscount(editedCourseDto.getDiscount());
                // ... (update other fields as needed)

                // Step 3: Save the updated course to the repository
                courseRepository.save(existingCourse);
                if (editedCourseDto.getDiscount() > 0){
                    notificationService.sendNotification(userRepository.allUserId(Role.USER), "Diskon "+editedCourseDto.getDiscount()+"% untuk course "+editedCourseDto.getName()+ " Buruan beli sekarang");
                }

                // Step 4: Return a successful response
                response.setError(false);
                response.setMessage(ResponseUtils.MESSAGE_SUCCESS_UPDATE_DATA_COURSE + courseId);
                response.setData(convertToDto(existingCourse));
            } else {
                // The course with the provided courseId was not found
                response.setError(true);
                response.setMessage(FAILED_GET_DATA_COURSE + courseId);
                response.setData(null);
            }
        } catch (Exception e) {
            // Log the exception or handle it as needed
            log.error("Error editing course with ID " + courseId, e);

            response.setError(true);
            response.setMessage(FAILED);
            response.setData(null);
        }
        return response;
    }

    public Response<String> deleteCourse (String code){
        Response<String> response = new Response<>();
        try {
            courseRepository.deleteById(code);
            response.setError(false);
            response.setMessage("Course with code " + code + " deleted successfully");

        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to dellete course with code " + code);
                }
        return response;
    }

    @Transactional(readOnly = true)
    @Override
    public Response<List<CourseDto>> filter (String type){
        Response<List<CourseDto>> responses = new Response<>();
        try{
            List<CourseDto> filteredCourses = courseRepository.filterByType(Type.valueOf(type.toUpperCase()));

            filteredCourses.forEach(courseDto -> courseDto.setRating(getRating(courseDto.getCode())));
            responses.setError(false);
            responses.setData(filteredCourses);
            responses.setMessage(SUCCESS);
        }catch (Exception e){
            responses.setError(true);
            responses.setMessage(FAILED);
            responses.setData(null);
        }
        return responses;
    }

    private CourseDto convertToDto (Course course){
        CourseDto dto = new CourseDto();
        BeanUtils.copyProperties(course, dto);
        dto.setCategory(course.getCategory().getName());
        dto.setLevel(course.getLevel());
        dto.setType(course.getType());
        dto.setRating(getRating(course.getCode()));
        dto.setImage(course.getCategory().getImage());
        return dto;
    }

    private Float getRating (String courseCode){
        List<Integer> ratings = paymentRepository.getListRating(courseCode);
        if (ratings.isEmpty()) {
            return null;
        } else {
            double sumRating = 0;
            for (Integer rating : ratings) {
                sumRating += rating;
            }
            return (float) (sumRating / ratings.size());
        }
    }
}
