package org.binar.pragosacademyapi.service.impl;

import org.binar.pragosacademyapi.entity.User;
import org.binar.pragosacademyapi.entity.UserVerification;
import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.enumeration.Role;
import org.binar.pragosacademyapi.repository.UserRepository;
import org.binar.pragosacademyapi.repository.UserVerificationRepository;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Random;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private JavaMailSender mailSender;
    private final Random random = new Random();
    @Autowired
    private UserVerificationRepository userVerificationRepository;

    @Override
    public Response<UserDto> getProfile() {
        User user = userRepository.findByEmail(getEmailUserContext());
        Response<UserDto> response = new Response<>();
        UserDto userDto = new UserDto();
        userDto.setName(user.getName());
        userDto.setCity(user.getCity());
        userDto.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDto.setEmail(user.getEmail());
        userDto.setCountry(user.getCountry());
        userDto.setPhone(user.getPhone());
        userDto.setImageProfile(null);
        response.setError(false);
        response.setMessage("Berhasil");
        response.setData(userDto);
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

    @Override
    public Response<String> update(RegisterRequest updateUser) {
        return null;
    }

    @Override
    public Response<String> update(String password) {
        return null;
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

    private void sendEmail(String email, Integer code) throws MessagingException, UnsupportedEncodingException {
        String subject = "Code verifikasi Pragos Academy";
        String content = "Kode verifikasi anda: "+ code + " kode verifikasi akan expired dalam 5 menit. Jangan kirimkan kode ini kesiapapun jika tidak mendaftar di pragos academy.";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom("gunawann.dev@gmail.com", "Pragos Academy");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content);
        mailSender.send(message);
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
