package org.binar.pragosacademyapi.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChapterRequest {

    private String chapterName;
    private String title;
    private String video;
    private String material;
}
