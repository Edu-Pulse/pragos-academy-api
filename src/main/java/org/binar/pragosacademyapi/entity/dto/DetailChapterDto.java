package org.binar.pragosacademyapi.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DetailChapterDto {
    private Long id;
    private String name;
    private String video;
    private String material;
    private Boolean isDone;
}
