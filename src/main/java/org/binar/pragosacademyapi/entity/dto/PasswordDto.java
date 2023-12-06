package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor


public class PasswordDto {
    private String email;
    private String oldPassword;
    private String newPassword;


}
