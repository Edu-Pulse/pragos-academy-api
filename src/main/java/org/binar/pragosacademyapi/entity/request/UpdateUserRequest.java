package org.binar.pragosacademyapi.entity.request;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UpdateUserRequest {
    private String name;
    private String email;
    private String phone;
    private String city;
    private String country;
    private MultipartFile file;
}
