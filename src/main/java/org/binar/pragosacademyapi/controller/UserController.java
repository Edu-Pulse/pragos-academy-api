package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.entity.dto.UserDto;
import org.binar.pragosacademyapi.entity.request.UpdateUserRequest;
import org.binar.pragosacademyapi.entity.response.Response;
import org.binar.pragosacademyapi.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/user")
@RestController
public class UserController {

    private final UserService userService;
    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @GetMapping(
           produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<UserDto>>getDataUSer(){
        return ResponseEntity.ok(userService.getProfile());
    }

   @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    @PostMapping(
            value = "/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<Response<String>> updateUser(@ModelAttribute UpdateUserRequest request){
        return ResponseEntity.ok(userService.update(request));
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping(
            value = "/detailchapter/setdone/{id}",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<String> setDoneDetailChapter(@PathVariable Long id){
        return ResponseEntity.ok(userService.setDoneChapter(id));
    }

}
