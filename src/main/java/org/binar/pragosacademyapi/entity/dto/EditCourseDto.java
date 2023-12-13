package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EditCourseDto {
    private String name;
    private String description;
    private String intended;
    private String lecturer;
    private String level;
    private String type;
    private Integer price;
    private Integer discount;
}
