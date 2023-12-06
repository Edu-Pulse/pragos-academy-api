package org.binar.pragosacademyapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.*;
import org.binar.pragosacademyapi.entity.dto.ChapterDto;
import org.binar.pragosacademyapi.entity.dto.CourseDetailDto;
import org.binar.pragosacademyapi.entity.dto.CourseDto;
import org.binar.pragosacademyapi.entity.dto.DetailChapterDto;
import org.binar.pragosacademyapi.entity.request.CourseRequest;
import org.binar.pragosacademyapi.entity.request.PaymentRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.CourseStatus;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;
import org.binar.pragosacademyapi.repository.*;
import org.binar.pragosacademyapi.service.CourseService;
import org.binar.pragosacademyapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.BeanUtils;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private UserServiceImpl userService;
    @Autowired
    private UserDetailChapterRepository userDetailChapterRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private NotificationService notificationService;

    @Override
    public Response<List<CourseDto>> listAllCourse() {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<CourseDto> courseDtos = courseRepository.findAll().stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());

            response.setError(false);
            response.setMessage("Success to get all courses");
            response.setData(courseDtos);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to get all courses");
            response.setData(null);
        }
        return response;
    }


    @Override
    public Response<CourseDetailDto> courseDetail(String courseCode) {
        Response<CourseDetailDto> response = new Response<>();
        User user = userRepository.findByEmail(userService.getEmailUserContext());
        try {
            if (user != null){
                Course course = paymentRepository.detailCourse(courseCode, user.getEmail());
                if (course != null){
                    List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), detailChapter.getVideo(), detailChapter.getMaterial(), userDetailChapterRepository.existsByUserIdAndDetailChapter_IdAndAndIsDone(user.getId(), detailChapter.getId(), true))).collect(Collectors.toList()))).collect(Collectors.toList());
                    response.setError(false);
                    response.setMessage("Success to get data "+ courseCode);
                    response.setData(
                            new CourseDetailDto(course.getCode(), course.getCategory().getImage(),course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()),courseRepository.getCountDetailChapter(courseCode), courseRepository.getCountDetailChapterDone(courseCode, user.getEmail()), chapters)
                    );
                }else {
                    course = courseRepository.findByCode(courseCode);
                    if (course != null){
                        List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), null, null, null)).collect(Collectors.toList()))).collect(Collectors.toList());
                        response.setError(false);
                        response.setMessage("Success to get data "+ courseCode);
                        response.setData(
                                new CourseDetailDto(course.getCode(), course.getCategory().getImage(),course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()),courseRepository.getCountDetailChapter(courseCode), null, chapters)
                        );
                    }else {
                        response.setError(true);
                        log.error(userService.getEmailUserContext());
                        response.setMessage("Failed to get data "+ courseCode+ " not found");
                        response.setData(null);
                    }
                }
            }else {
                Course course = courseRepository.findByCode(courseCode);
                if (course != null){
                    List<ChapterDto> chapters = course.getChapters().stream().map(chapter -> new ChapterDto(chapter.getCapther(), chapter.getDetailChapters().stream().map(detailChapter -> new DetailChapterDto(detailChapter.getId(), detailChapter.getName(), null, null, null)).collect(Collectors.toList()))).collect(Collectors.toList());
                    response.setError(false);
                    response.setMessage("Success to get data "+ courseCode);
                    response.setData(
                            new CourseDetailDto(course.getCode(), course.getCategory().getImage(),course.getCategory().getName(), course.getName(), course.getDescription(), course.getIntended().split(","), course.getLecturer(), course.getLevel().toString(), course.getType().toString(), course.getPrice(), course.getDiscount(), getRating(course.getCode()), courseRepository.getCountDetailChapter(courseCode), null,chapters)
                    );
                }else {
                    response.setError(true);
                    log.error(userService.getEmailUserContext());
                    response.setMessage("Failed to get data "+ courseCode+ " not found");
                    response.setData(null);
                }
            }
        }catch (Exception e){
            response.setError(true);
            log.error(e.getMessage());
            response.setMessage("Failed to get data "+ courseCode);
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> enrollCourse(String courseCode) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(courseCode);
            User user = userRepository.findByEmail(userService.getEmailUserContext());
            if (course != null){
                Payment checkPayment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
                if (checkPayment == null){
                    if (course.getPrice() == 0){
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
                        response.setMessage("Success");
                        response.setData("Berhasil enroll course : "+courseCode);
                        notificationService.sendNotification(user.getId(), "Kamu telah terdaftar ke kelas "+ course.getName()+" Semoga ilmu yang akan dipelajari dapat bermanfaat didunia maupun akhirat");
                    }else {
                        response.setError(true);
                        response.setMessage("Failed");
                        response.setData("Course ini berbayar");
                    }
                }else {
                    response.setError(true);
                    response.setMessage("Failed");
                    response.setData("Kamu sudah enroll kelas ini");
                }
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData("Course dengan code "+courseCode+" tidak ditemukan");
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Terjadi kesalahan");
        }
        return response;
    }
  
    @Override
    public Response<String> enrollPaidCourse(String courseCode, PaymentRequest paymentRequest) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(courseCode);
            User user = userRepository.findByEmail(userService.getEmailUserContext());
            if (course != null){
                Payment checkPayment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
                if (checkPayment == null){
                    log.info(paymentRequest.getCardNumber());
                    if (paymentRequest.getCardNumber().length() == 16) {
                        Payment payment = new Payment();
                        payment.setUser(user);
                        payment.setCourse(course);
                        double discount = (course.getPrice() * ((double) course.getDiscount() / 100));
                        log.info("Diskon :"+ discount);
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
                        response.setMessage("Sukses");
                        response.setData("Berhasil mendaftar kursus: " + courseCode);
                        notificationService.sendNotification(user.getId(), "Kamu telah terdaftar ke kelas "+ course.getName()+" Semoga ilmu yang akan dipelajari dapat bermanfaat didunia maupun akhirat");
                    } else {
                        response.setError(true);
                        response.setMessage("Gagal");
                        response.setData("Detail kartu tidak valid");
                    }
                } else {
                    response.setError(true);
                    response.setMessage("Gagal");
                    response.setData("Anda sudah mendaftar kursus ini");
                }
            } else {
                response.setError(true);
                response.setMessage("Gagal");
                response.setData("Kursus dengan kode " + courseCode + " tidak ditemukan");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
            response.setError(true);
            response.setMessage("Gagal");
            response.setData("Terjadi kesalahan");
        }
        return response;
    }
  
    @Transactional(readOnly = true)
    @Override
    public Response<List<CourseDto>> search(String courseName) {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<CourseDto> courseDtoList = courseRepository.searchByCourseName("%"+courseName+"%");
            courseDtoList.forEach(courseDto -> courseDto.setRating(getRating(courseDto.getCode())));
            response.setError(false);
            response.setMessage("Success get data");
            response.setData(courseDtoList);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed get data");
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
            response.setMessage("Success get data");
            response.setData(courseDtoList);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed get data");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<List<CourseDto>> getCoursesByUserAll() {
        Response<List<CourseDto>> response = new Response<>();
        try {
            List<Payment> payments = paymentRepository.paymentByUserAndStatus(userService.getEmailUserContext());

            List<CourseDto> courseDtos = payments.stream()
                    .map(payment -> convertToDto(payment.getCourse()))
                    .collect(Collectors.toList());

            response.setError(false);
            response.setMessage("Success to get courses by user");
            response.setData(courseDtos);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to get courses by user");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> createCourse(CourseRequest request) {
        Response<String> response = new Response<>();
        try {
            Course course = courseRepository.findByCode(request.getCourseCode());
            if (course == null){
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
                response.setMessage("Success");
                response.setData("Success add course");
            }else {
                response.setError(true);
                response.setMessage("Failed");
                response.setData("Course code already exist");
            }
        }catch (Exception e){
            response.setError(true);
            response.setData(null);
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
            response.setMessage("Success to get courses by user and status");
            response.setData(courseDtos);
        } catch (Exception e) {
            response.setError(true);
            response.setMessage("Failed to get courses by user and status");
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
            User user = userRepository.findByEmail(userService.getEmailUserContext());
            Payment payment = paymentRepository.findByUser_IdAndCourse_Code(user.getId(), courseCode);
            if (payment != null){
                payment.setRating(rating);
                paymentRepository.save(payment);
                response.setError(false);
                response.setMessage("Success");
                response.setData("Berhasil menambahkan rating");
                notificationService.sendNotification(user.getId(), "Terimakasih telah memberikan rating ke course "+ payment.getCourse().getName());
            }else {
                response.setError(true);
                response.setMessage("Kamu belum enroll kelas ini");
                response.setData(null);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }
        return response;
    }
  
    @Transactional(readOnly = true)
    @Override
    public Response<List<CourseDto>> filter(String type) {
        List<CourseDto> filteredCourses = courseRepository.filterByType(Type.valueOf(type.toUpperCase()));
        filteredCourses.forEach(courseDto -> courseDto.setRating(getRating(courseDto.getCode())));

        Response<List<CourseDto>> responses = new Response<>();
        responses.setData(filteredCourses);
        responses.setMessage("Course Filter Sucsess");
        return responses;
    }

    private CourseDto convertToDto(Course course) {
        CourseDto dto = new CourseDto();
        BeanUtils.copyProperties(course, dto);
        dto.setCategory(course.getCategory().getName());
        dto.setLevel(course.getLevel());
        dto.setType(course.getType());
        dto.setRating(getRating(course.getCode()));
        dto.setImage(course.getCategory().getImage()); // asumsikan category memiliki properti image
        return dto;
    }

    private Float getRating(String courseCode){
        List<Integer> ratings = paymentRepository.getListRating(courseCode);
        if (ratings.isEmpty()){
            return null;
        }else {
            double sumRating = 0;
            for (Integer rating : ratings) {
                sumRating += rating;
            }
            return (float) (sumRating/ratings.size());
        }
    }
}
