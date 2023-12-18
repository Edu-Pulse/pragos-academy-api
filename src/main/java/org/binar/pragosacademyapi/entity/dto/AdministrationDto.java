package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AdministrationDto {
    private Integer totalUser;
    private Integer totalCourse;
    private Integer totalCoursePremium;
}
