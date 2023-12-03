package org.binar.pragosacademyapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseRequest {
    private String courseCode;
    private String courseName;
    private Integer category;
    private String description;
    private String intended;
    private String lecturer;
    private String type;
    private String level;
    private Integer price;
    private Integer discount;
}
