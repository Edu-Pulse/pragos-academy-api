package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class CourseDetailDto {
    private String code;
    private String image;
    private String category;
    private String name;
    private String description;
    private String[] ditunjukanUntuk;
    private String lecturer;
    private String level;
    private String type;
    private Integer price;
    private Integer discount;
    private Float rating;
    private Integer totalMaterial;
    private Integer doneMaterial;
    private List<ChapterDto> chapters;
}
