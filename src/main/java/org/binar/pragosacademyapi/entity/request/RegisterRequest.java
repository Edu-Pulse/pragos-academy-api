package org.binar.pragosacademyapi.entity.request;

import lombok.Data;

@Data
public class RegisterRequest {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String country;
}
