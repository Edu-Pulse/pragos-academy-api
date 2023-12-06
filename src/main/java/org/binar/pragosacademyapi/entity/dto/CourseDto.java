package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.binar.pragosacademyapi.enumeration.Level;
import org.binar.pragosacademyapi.enumeration.Type;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CourseDto {
    private String code;
    private String image;
    private String category;
    private String name;
    private String description;
    private String lecturer;
    private Level level;
    private Type type;
    private Integer price;
    private Integer discount;
    private Float rating = 0f;

    public CourseDto(String code, String image, String category, String name, String description, String lecturer, Level level, Type type, Integer price, Integer discount){
        this.code = code;
        this.image = image;
        this.category = category;
        this.name = name;
        this.description = description;
        this.lecturer = lecturer;
        this.level = level;
        this.type = type;
        this.price = price;
        this.discount = discount;
    }
}
