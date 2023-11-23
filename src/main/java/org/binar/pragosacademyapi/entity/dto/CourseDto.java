package org.binar.pragosacademyapi.entity.dto;

import lombok.Data;

@Data
public class CourseDto {
    private String code;
    private byte[] image;
    private String category;
    private String name;
    private String description;
    private String lecturer;
    private String level;
    private String type;
    private Integer price;
    private Integer discount;
    private Float rating;
}
