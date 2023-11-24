package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.config.TokenProvider;
import org.binar.pragosacademyapi.entity.request.LoginRequest;
import org.binar.pragosacademyapi.entity.request.RegisterRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider tokenProvider;
    private final UserService userService;
    @Autowired
    public UserController(AuthenticationManager authenticationManager, TokenProvider tokenProvider, UserService userService){
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @PostMapping(
            value = "/login",
            consumes = MediaType.APPLICATION_JSON_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<Map<String, String>>> login(@RequestBody LoginRequest request){
        Response<Map<String, String>> response = new Response<>();
        if (userService.checkIsEnable(request.getEmail())){
            final Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
            final String token = tokenProvider.generateToken(authentication);
            response.setError(false);
            response.setMessage("Login Berhasil");
            Map<String, String> dataToken = new HashMap<>();
            dataToken.put("token", token);
            response.setData(dataToken);
        }else {
            response.setError(true);
            response.setMessage("Anda belum melakukan registrasi atau verifikasi email");
            response.setData(null);
        }
        return ResponseEntity.ok(response);
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
            value = "/generate-code-verification",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> generateCode(@RequestParam String email){
        return ResponseEntity.ok(userService.generateCodeVerification(email));
    }

    @PutMapping(
            value = "/verification"
    )
    public ResponseEntity<Response<String>> verification(@RequestParam String email, @RequestParam Integer code){
        return ResponseEntity.ok(userService.verification(email, code));
    }

    @PutMapping(
            value = "/user/update",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> updateuser(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(userService.update(request));
    }

}
