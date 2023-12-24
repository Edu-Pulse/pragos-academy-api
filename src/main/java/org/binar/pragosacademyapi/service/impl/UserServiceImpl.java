package org.binar.pragosacademyapi.service.impl;

import com.cloudinary.Cloudinary;
import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.*;
import org.binar.pragosacademyapi.entity.dto.PasswordDto;
import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.request.UpdateUserRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.*;
import org.binar.pragosacademyapi.service.CloudinaryService;
import org.binar.pragosacademyapi.service.NotificationService;
import org.binar.pragosacademyapi.service.UserService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final UserRepository userRepository;
    private final JavaMailSender mailSender;
    private final UserVerificationRepository userVerificationRepository;
    private final UserDetailChapterRepository userDetailChapterRepository;
    private final DetailChapterRepository detailChapterRepository;
    private final NotificationService notificationService;
    private final CloudinaryService cloudinaryService;
    private final CourseRepository courseRepository;
    private static final String MESSAGE_SUCCESS = ResponseUtils.MESSAGE_SUCCESS;
    private static final String MESSAGE_FAILED = ResponseUtils.MESSAGE_FAILED;
    private static final String FAILED = ResponseUtils.FAILED;
    private static final String MESSAGE_SUCCESS_UPDATE_USER = ResponseUtils.MESSAGE_SUCCESS_UPDATE_USER;
    private static final String MESSAGE_FAILED_UPDATE_USER = ResponseUtils.MESSAGE_FAILED_UPDATE_USER;

    @Autowired
    public UserServiceImpl(BCryptPasswordEncoder bCryptPasswordEncoder,
                           UserRepository userRepository,
                           JavaMailSender mailSender,
                           UserVerificationRepository userVerificationRepository,
                           UserDetailChapterRepository userDetailChapterRepository,
                           DetailChapterRepository detailChapterRepository,
                           NotificationService notificationService,
                           CloudinaryService cloudinaryService,
                           CourseRepository courseRepository){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
        this.userVerificationRepository = userVerificationRepository;
        this.userDetailChapterRepository = userDetailChapterRepository;
        this.detailChapterRepository = detailChapterRepository;
        this.notificationService = notificationService;
        this.cloudinaryService = cloudinaryService;
        this.courseRepository = courseRepository;
    }

    @Value("${app.name}")
    private String appName;
    @Value("${spring.mail.username}")
    private String emailSmtp;
    @Value("${base.url.fe}")
    private String baseUrlFe;
    private final Random random = new Random();

    @Override
    public Response<UserDto> getProfile() {
        Response<UserDto> response = new Response<>();
        try {
            User user = userRepository.findByEmail(getEmailUserContext()).orElse(null);
            if (user != null){
                UserDto userDto = new UserDto();
                userDto.setImageProfile(user.getImageProfile());
                userDto.setName(user.getName());
                userDto.setCity(user.getCity());
                userDto.setEmail(user.getEmail());
                userDto.setCountry(user.getCountry());
                userDto.setPhone(user.getPhone());
                response.setError(false);
                response.setMessage(MESSAGE_SUCCESS);
                response.setData(userDto);
            }else {
                response.setError(true);
                response.setMessage(MESSAGE_FAILED);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
        }
        return response;
    }

    @Override
    public Response<String> register(RegisterRequest user) {
        Response<String> response = new Response<>();

        try{

            User existingUser = userRepository.findByEmail(user.getEmail()).orElse(null);
            if (existingUser == null){
                User newuser = new User();
                newuser.setName(user.getName());
                newuser.setEmail(user.getEmail());
                newuser.setPhone(user.getPhone());
                newuser.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                newuser.setCity(user.getCity());
                newuser.setCountry(user.getCountry());
                newuser.setRole(Role.USER);
                newuser.setIsEnable(false);
                Integer code = random.nextInt(9999 - 1000) + 1000;
                UserVerification userVerification = new UserVerification();
                userVerification.setUser(newuser);
                userVerification.setVerificationCode(code);
                userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
                newuser.setUserVerification(userVerification);

                userRepository.save(newuser);
                response.setError(false);
                response.setMessage(MESSAGE_SUCCESS);

                sendEmail(user.getEmail(), code);
                response.setData("Berhasil register. Silahkan cek email untuk kode verifikasi");
            }else {
                boolean isUserEnabled = existingUser.getIsEnable();
                if (isUserEnabled){
                    response.setError(true);
                    response.setMessage(FAILED);
                    response.setData("Email sudah didaftarkan");
                }else {
                    response.setError(true);
                    response.setMessage(FAILED);
                    response.setData("Email belum diverifikasi");
                }
            }

        }catch (Exception e){
            response.setError(true);
            response.setMessage(FAILED);
            response.setData("Terjadi kesalahan. Silahkan coba dengan email atau nomor telepon yang berbeda");
        }
        return response;
    }

    @Transactional
    @Override
    public Response<String> update(UpdateUserRequest updateUser) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(getEmailUserContext()).orElse(null);
            if (user != null){
                MultipartFile file = updateUser.getFile();
                if (!file.isEmpty()){
                    user.setImageProfile(cloudinaryService.uploadImage(file));
                }
                user.setName(updateUser.getName());
                user.setCity(updateUser.getCity());
                user.setCountry(updateUser.getCountry());
                if (Objects.equals(updateUser.getEmail(), user.getEmail())){
                    user.setEmail(updateUser.getEmail());
                    if (Objects.equals(updateUser.getPhone(), user.getPhone())){
                        user.setPhone(updateUser.getPhone());
                        userRepository.save(user);
                        response.setError(false);
                        response.setMessage(MESSAGE_SUCCESS);
                        response.setData(MESSAGE_SUCCESS_UPDATE_USER + user.getId());
                        notificationService.sendNotification(user.getId(), ResponseUtils.MESSAGE_SUCCESS_UPDATE_USER_NOTIFICATION);
                    }else {
                        response = checkUserPhone(user, updateUser);
                    }
                }else {
                    response = checkUserEmail(user, updateUser);
                }
            }else {
                response.setError(true);
                response.setMessage(MESSAGE_FAILED);
            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
        }
        return response;
    }

    private Response<String> checkUserPhone(User user, UpdateUserRequest updateUser){
        Response<String> response = new Response<>();
        try {
            user.setPhone(updateUser.getPhone());
            userRepository.save(user);
            response.setError(false);
            response.setMessage(MESSAGE_SUCCESS);
            response.setData(MESSAGE_SUCCESS_UPDATE_USER + user.getId());
            notificationService.sendNotification(user.getId(), ResponseUtils.MESSAGE_SUCCESS_UPDATE_USER_NOTIFICATION);
        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED_UPDATE_USER + user.getId());
            response.setData("No Telepon sudah didaftarkan. Silahkan gunakan no telepon yang lain");
        }
        return response;
    }
    private Response<String> checkUserEmail(User user, UpdateUserRequest updateUser){
        Response<String> response = new Response<>();
        try {
            user.setEmail(updateUser.getEmail());
            userRepository.save(user);
            response.setError(false);
            response.setMessage(MESSAGE_SUCCESS);
            response.setData(MESSAGE_SUCCESS_UPDATE_USER + user.getId());
            notificationService.sendNotification(user.getId(), "Data kamu berhasil diupdate");
        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED_UPDATE_USER + user.getId());
            response.setData("Email sudah didaftarkan. Silahkan gunakan email yang lain");
        }
        return response;
    }

    @Override
    public Response<String> generateCodeVerification(String email) {
        Response<String> response = new Response<>();
        Integer code = random.nextInt(9999 - 1000) + 1000;
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null){
                UserVerification userVerification = user.getUserVerification();
                userVerification.setVerificationCode(code);
                userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
                user.setUserVerification(userVerification);
                userRepository.save(user);

                sendEmail(email, code);
                response.setError(false);
                response.setMessage(MESSAGE_SUCCESS);
                response.setData("Kode verifikasi berhasil dikirim");
        }else {
                response.setError(true);
                response.setMessage("User dengan email: "+ email + " tidak ditemukan");
                response.setData(null);
            }

        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
            response.setData(null);
        }

        return response;
    }

    @Transactional
    @Override
    public Response<String> verification(String email, Integer code) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null){
                UserVerification userVerification = userVerificationRepository.findByUser_Id(user.getId());
                if (Objects.equals(code, userVerification.getVerificationCode()) && !LocalDateTime.now().isAfter(userVerification.getExpiredAt())){
                    user.setIsEnable(true);
                    userRepository.save(user);
                    response.setError(false);
                    response.setMessage(MESSAGE_SUCCESS);
                    response.setData("Email berhasil diverifikasi");
                    notificationService.sendNotification(user.getId(), "Selamat datang di EduPulse "+ user.getName()+"! mari belajar bersama course kami");
                }else {
                    response.setError(false);
                    response.setMessage("Kode verifikasi salah atau sudah expired");
                    response.setData(null);
                }
            }else {
                response.setError(true);
                response.setMessage("User tidak ditemukan");
                response.setData(null);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
            response.setData(null);
        }
        return response;
    }

    @Override
    public boolean checkIsEnable(String email) {
        try {
            User checkUser = userRepository.findByEmail(email).orElse(null);
            if (checkUser != null){
                return checkUser.getIsEnable();
            }else {
                return false;
            }
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public String setDoneChapter(String courseCode, Long detailChapterId) {
        try {
            User user = userRepository.findByEmail(getEmailUserContext()).orElse(null);
            DetailChapter detailChapter = detailChapterRepository.findById(detailChapterId).orElse(null);
            if (detailChapter != null){
                boolean checkIsPresent = userDetailChapterRepository.existsByUserIdAndDetailChapter_IdAndAndIsDone(user.getId(), detailChapterId, true);
                if (!checkIsPresent){
                    UserDetailChapter userDetailChapter = new UserDetailChapter();
                    userDetailChapter.setUser(user);
                    userDetailChapter.setDetailChapter(detailChapter);
                    userDetailChapter.setIsDone(true);
                    userDetailChapterRepository.save(userDetailChapter);
                    if (courseRepository.getCountDetailChapter(courseCode) == courseRepository.getCountDetailChapterDone(courseCode, user.getEmail())){
                        Course course = courseRepository.findByCode(courseCode);
                        notificationService.sendNotification(user.getId(), "Selamat kamu telah berhasil menyelesaikan Course"+ course.getName());
                    }
                }
                return MESSAGE_SUCCESS;
            }
            return "Detail chapter dengan id: "+ detailChapterId+" Tidak ditemukan";

        }catch (Exception e){
            log.error(e.getMessage());
            return MESSAGE_FAILED;
        }
    }

    @Override
    public Response<String> resetPassword(Integer verificationCode, String email, String newPassword) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null){
                UserVerification userVerification = userVerificationRepository.findByUser_Id(user.getId());
                if (Objects.equals(verificationCode, userVerification.getVerificationCode()) && !LocalDateTime.now().isAfter(userVerification.getExpiredAt())){

                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    userRepository.save(user);

                    response.setError(false);
                    response.setMessage(MESSAGE_SUCCESS);
                    response.setData("Password berhasil diubah. Silahkan login dengan password yang baru");
                    notificationService.sendNotification(user.getId(), "Password kamu baru saja diganti");
                }else {
                    response.setError(true);
                    response.setMessage("Kode verifikasi salah atau sudah expired");
                    response.setData(null);
                }
            }else {
                response.setError(true);
                response.setMessage("User tidak ditemukan");
                response.setData(null);
            }
        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> forgotPassword(String email) {
        Response<String> response = new Response<>();
        Integer code = random.nextInt(9999 - 1000) + 1000;
        try {
            User user = userRepository.findByEmail(email).orElse(null);
            if (user != null){
                UserVerification userVerification = user.getUserVerification();
                userVerification.setVerificationCode(code);
                userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
                user.setUserVerification(userVerification);
                userRepository.save(user);

                sendEmailForgotPassword(email, code);
                response.setError(false);
                response.setMessage(MESSAGE_SUCCESS);
                response.setData("Tautan ganti password sudah dikirim ke email kamu");
            }else {
                response.setError(true);
                response.setMessage("User dengan email: "+ email + " tidak ditemukan");
                response.setData(null);
            }

        }catch (Exception e){
            response.setError(true);
            response.setMessage(MESSAGE_FAILED);
            response.setData(null);
        }

        return response;
    }

    @Override
    public Response<String> changePassword(PasswordDto request) {
        Response<String> response = new Response<>();
            try {
                User user = userRepository.findByEmail(getEmailUserContext()).orElse(null);
                if (user != null){
                    if (bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())){
                        String encodedNewPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
                        user.setPassword(encodedNewPassword);
                        userRepository.save(user);
                        response.setError(false);
                        response.setData("Change Password Successfully");
                        response.setMessage(MESSAGE_SUCCESS);
                        notificationService.sendNotification(user.getId(), "Password telah diubah");
                    }else {
                        response.setError(true);
                        response.setMessage("Password lama anda salah");
                    }
                }else {
                    response.setError(true);
                    response.setMessage(MESSAGE_FAILED);
                }
            }
            catch ( Exception e ){
                response.setError(true);
                response.setMessage(MESSAGE_FAILED);
            }

        return response;
    }

    private void sendEmail(String email, Integer code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Kode verifikasi Pragos Academy";
        String content = "Kode verifikasi anda: <b>"+ code + "</b> kode verifikasi akan expired dalam 5 menit. Jangan kirimkan kode ini kesiapapun jika tidak mendaftar di pragos academy.";

        sendMail(email, subject, content);
    }

    private void sendEmailForgotPassword(String email, Integer code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Tautan ganti password";
        String content = "Berikut kode verifikasi untuk reset password: "+code+" <br>" +
                "Klik tautan untuk ganti password anda <a href=\""+baseUrlFe+"/auth/reset"+"\">Ganti password</a><br>" +
                "<b>Kode verifikasi akan expired dalam 5 menit</b>";

        sendMail(email, subject, content);
    }

    private void sendMail(String email, String subject, String content) throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(emailSmtp, appName);
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    protected String getEmailUserContext(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)){
            return authentication.getName();
        }else {
            return null;
        }
    }
}
