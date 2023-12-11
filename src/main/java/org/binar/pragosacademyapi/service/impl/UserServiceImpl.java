package org.binar.pragosacademyapi.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.*;
import org.binar.pragosacademyapi.entity.dto.PasswordDto;
import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.request.UpdateUserRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.DetailChapterRepository;
import org.binar.pragosacademyapi.repository.UserDetailChapterRepository;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.binar.pragosacademyapi.repository.UserVerificationRepository;
import org.binar.pragosacademyapi.service.NotificationService;
import org.binar.pragosacademyapi.service.UserService;
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

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    @Autowired
    private UserVerificationRepository userVerificationRepository;
    @Autowired
    private UserDetailChapterRepository userDetailChapterRepository;
    @Autowired
    private DetailChapterRepository detailChapterRepository;
    @Value("${spring.mail.username}")
    private String EMAIL;
    @Value("${base.url.fe}")
    private String BASE_URL_FE;
    @Autowired
    private NotificationService notificationService;
    private final Path root = Paths.get("/app/uploads");
    private final Random random = new Random();

    @Override
    public Response<UserDto> getProfile() {
        Response<UserDto> response = new Response<>();
        try {
            User user = userRepository.findByEmail(getEmailUserContext());
            UserDto userDto = new UserDto();
            if (user.getImageProfile() != null){
                Path file = root.resolve(user.getImageProfile());
                userDto.setImageProfile(Files.readAllBytes(file));
            }
            userDto.setName(user.getName());
            userDto.setCity(user.getCity());
            userDto.setPassword(null);
            userDto.setEmail(user.getEmail());
            userDto.setCountry(user.getCountry());
            userDto.setPhone(user.getPhone());
            response.setError(false);
            response.setMessage("Berhasil");
            response.setData(userDto);
        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan"+ e.getMessage());
        }
        return response;
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
            newuser.setIsEnable(false);
            Integer code = random.nextInt(9999 - 1000) + 1000;
            UserVerification userVerification = new UserVerification();
            userVerification.setUser(newuser);
            userVerification.setVerificationCode(code);
            userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
            newuser.setUserVerification(userVerification);

            userRepository.save(newuser);
            response.setError(true);
            response.setMessage("Success");

            sendEmail(user.getEmail(), code);
            response.setData("Berhasil register. Silahkan cek email untuk kode verifikasi");

        }catch (Exception e){
            response.setError(true);
            response.setMessage("Failed");
            response.setData("Terjadi kesalahan. Silahkan coba dengan email atau nomor telepon yang berbeda");
        }
        return response;
    }

    @Transactional
    @Override
    public Response<String> update(UpdateUserRequest updateUser) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(getEmailUserContext());
            MultipartFile file = updateUser.getFile();
            if (!file.isEmpty()){
                try(InputStream inputStream = file.getInputStream()) {
                    Files.copy(inputStream, this.root.resolve(user.getId() + file.getOriginalFilename()), StandardCopyOption.REPLACE_EXISTING);
                }
                user.setImageProfile(user.getId()+file.getOriginalFilename());
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
                    response.setMessage("Success");
                    response.setData("Success update data");
                    notificationService.sendNotification(user.getId(), "Data kamu berhasil diupdate");
                }else {
                    try {
                        user.setPhone(updateUser.getPhone());
                        userRepository.save(user);
                        response.setError(false);
                        response.setMessage("Success");
                        response.setData("Success update data");
                        notificationService.sendNotification(user.getId(), "Data kamu berhasil diupdate");
                    }catch (Exception e){
                        response.setError(true);
                        response.setMessage("Failed to update data");
                        response.setData("No Telepon sudah didaftarkan. Silahkan gunakan no telepon yang lain");
                    }
                }
            }else {
                try {
                    user.setEmail(updateUser.getEmail());
                    userRepository.save(user);
                    response.setError(false);
                    response.setMessage("Success");
                    response.setData("Success update data");
                    notificationService.sendNotification(user.getId(), "Data kamu berhasil diupdate");
                }catch (Exception e){
                    response.setError(true);
                    response.setMessage("Failed to update data");
                    response.setData("Email sudah didaftarkan. Silahkan gunakan email yang lain");
                }
            }
        }catch (Exception e){
            log.error(e.getMessage());
            response.setError(true);
            response.setMessage("Failed to update data "+e.getMessage());
            response.setData("Terjadi kesalahan");
        }
        return response;
    }

    @Override
    public Response<String> generateCodeVerification(String email) {
        Response<String> response = new Response<>();
        Integer code = random.nextInt(9999 - 1000) + 1000;
        try {
            User user = userRepository.findByEmail(email);
            if (user != null){
                UserVerification userVerification = user.getUserVerification();
                userVerification.setVerificationCode(code);
                userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
                user.setUserVerification(userVerification);
                userRepository.save(user);

                sendEmail(email, code);
                response.setError(false);
                response.setMessage("Success");
                response.setData("Kode verifikasi berhasil dikirim");
        }else {
                response.setError(true);
                response.setMessage("User dengan email: "+ email + " tidak ditemukan");
                response.setData(null);
            }

        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }

        return response;
    }

    @Transactional
    @Override
    public Response<String> verification(String email, Integer code) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(email);
            if (user != null){
                UserVerification userVerification = userVerificationRepository.findByUser_Id(user.getId());
                if (Objects.equals(code, userVerification.getVerificationCode()) && !LocalDateTime.now().isAfter(userVerification.getExpiredAt())){
                    user.setIsEnable(true);
                    userRepository.save(user);
                    response.setError(false);
                    response.setMessage("Success");
                    response.setData("Email berhasil diverifikasi");
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
            response.setMessage("Terjadi kesalahan. Silahkan coba lagi");
            response.setData(null);
        }
        return response;
    }

    @Override
    public boolean checkIsEnable(String email) {
        try {
            User checkUser = userRepository.findByEmail(email);
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
    public String setDoneChapter(Long detailChapterId) {
        try {
            User user = userRepository.findByEmail(getEmailUserContext());
            DetailChapter detailChapter = detailChapterRepository.findById(detailChapterId).orElse(null);
            if (detailChapter != null){
                UserDetailChapter userDetailChapter = new UserDetailChapter();
                userDetailChapter.setUser(user);
                userDetailChapter.setDetailChapter(detailChapter);
                userDetailChapter.setIsDone(true);
                userDetailChapterRepository.save(userDetailChapter);
                return "Success";
            }
            return "Detail chapter dengan id: "+ detailChapterId+" Tidak ditemukan";

        }catch (Exception e){
            log.error(e.getMessage());
            return "Terjadi kesalahan";
        }
    }

    @Override
    public Response<String> resetPassword(Integer verificationCode, String email, String newPassword) {
        Response<String> response = new Response<>();
        try {
            User user = userRepository.findByEmail(email);
            if (user != null){
                UserVerification userVerification = userVerificationRepository.findByUser_Id(user.getId());
                if (Objects.equals(verificationCode, userVerification.getVerificationCode()) && !LocalDateTime.now().isAfter(userVerification.getExpiredAt())){

                    user.setPassword(bCryptPasswordEncoder.encode(newPassword));
                    userRepository.save(user);

                    response.setError(false);
                    response.setMessage("Success");
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
            response.setMessage("Terjadi kesalahan. Silahkan coba lagi");
            response.setData(null);
        }
        return response;
    }

    @Override
    public Response<String> forgotPassword(String email) {
        Response<String> response = new Response<>();
        Integer code = random.nextInt(9999 - 1000) + 1000;
        try {
            User user = userRepository.findByEmail(email);
            if (user != null){
                UserVerification userVerification = user.getUserVerification();
                userVerification.setVerificationCode(code);
                userVerification.setExpiredAt(LocalDateTime.now().plusMinutes(5));
                user.setUserVerification(userVerification);
                userRepository.save(user);

                sendEmailForgotPassword(email, code);
                response.setError(false);
                response.setMessage("Success");
                response.setData("Tautan ganti password sudah dikirim ke email kamu");
            }else {
                response.setError(true);
                response.setMessage("User dengan email: "+ email + " tidak ditemukan");
                response.setData(null);
            }

        }catch (Exception e){
            response.setError(true);
            response.setMessage("Terjadi kesalahan");
            response.setData(null);
        }

        return response;
    }

    @Override
    public Response<String> changePassword(PasswordDto request) {
        Response<String> response = new Response<>();
        User user = userRepository.findByEmail(getEmailUserContext());
            try {
                if (bCryptPasswordEncoder.matches(request.getOldPassword(), user.getPassword())){
                    String encodedNewPassword = bCryptPasswordEncoder.encode(request.getNewPassword());
                    user.setPassword(encodedNewPassword);
                    userRepository.save(user);
                    response.setError(false);
                    response.setData("Change Password Succsess");
                    response.setMessage("Success");
                    notificationService.sendNotification(user.getId(), "Password telah diubah");
                }else {
                    response.setError(true);
                    response.setMessage("Password lama anda salah");
                }
            }
            catch ( Exception e ){
                response.setError(true);
                response.setMessage("Failed to change Password");
            }

        return response;
    }

    private void sendEmail(String email, Integer code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Kode verifikasi Pragos Academy";
        String content = "Kode verifikasi anda: <b>"+ code + "</b> kode verifikasi akan expired dalam 5 menit. Jangan kirimkan kode ini kesiapapun jika tidak mendaftar di pragos academy.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(EMAIL, "Pragos Academy");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(message);
    }

    private void sendEmailForgotPassword(String email, Integer code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Tautan ganti password";
        String content = "Klik tautan untuk ganti password anda <a href=\""+BASE_URL_FE+"reset-password?email="+email+"&verificationCode="+code+"\">Ganti password</a><br>" +
                "<b>Tautan akan expired dalam 5 menit</b>";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(EMAIL, "Pragos Academy");
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
