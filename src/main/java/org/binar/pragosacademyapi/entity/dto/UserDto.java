package org.binar.pragosacademyapi.entity.dto;

import lombok.Data;

@Data
public class UserDto {
    private String name;
    private String email;
    private String phone;
    private String password;
    private String city;
    private String country;
    private byte[] imageProfile;
}
