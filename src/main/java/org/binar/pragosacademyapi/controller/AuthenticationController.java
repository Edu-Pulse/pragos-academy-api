package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.config.TokenProvider;
import org.binar.pragosacademyapi.entity.request.LoginRequest;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

@RestController
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private TokenProvider tokenProvider;
    @Autowired
    private UserService userService;

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> login(@RequestBody LoginRequest request, HttpServletResponse servletResponse){
        Response<String> response = new Response<>();
        if (userService.checkIsEnable(request.getEmail())){
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = tokenProvider.generateToken(authentication);

            ResponseCookie cookie = ResponseCookie.from("COOKIE_AUTH", token)
                    .httpOnly(true)
                    .secure(false)
                    .path("/")
                    .build();

            servletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

            response.setError(false);
            response.setMessage("Login Berhasil");
            response.setData(authentication.getAuthorities().toString());
            return ResponseEntity.ok(response);
        }else {
            response.setError(true);
            response.setMessage("Anda belum melakukan registrasi atau verifikasi email");
            response.setData(null);
            return ResponseEntity.status(401).body(response);
        }
    }

    @PostMapping(
            value = "/register",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.register(request));
    }

    @GetMapping(
            value = "/generate-verification-code",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> generateCode(@RequestParam String email){
        return ResponseEntity.ok(userService.generateCodeVerification(email));
    }

    @PostMapping(
            value = "/verification-email",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> verification(@RequestParam String email, @RequestParam Integer code){
        return ResponseEntity.ok(userService.verification(email, code));
    }

    @GetMapping(
            value = "/forgot-password/{email}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> forgotPassword(@PathVariable String email){
        return ResponseEntity.ok(userService.forgotPassword(email));
    }

    @PostMapping(
            value = "/reset-password",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> resetPassword(@RequestParam String email, @RequestParam Integer verificationCode, @RequestParam String newPassword){
        return ResponseEntity.ok(userService.resetPassword(verificationCode, email, newPassword));
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    @RequestMapping(value = "/logout")
    public ResponseEntity<Void> logout(){

        return ResponseEntity.noContent().build();
    }
    @RequestMapping(
            value = "/logout-result",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> logoutResult(){
        return ResponseEntity.ok("Berhasil logout");
    }

}
