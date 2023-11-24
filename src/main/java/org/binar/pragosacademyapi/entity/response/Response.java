package org.binar.pragosacademyapi.entity.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Response <T>{

    private Boolean error;
    private String message;
    private T data;
}
