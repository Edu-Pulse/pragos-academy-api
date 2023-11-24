package org.binar.pragosacademyapi.entity.response;

import lombok.Data;

@Data
public class Response <T>{

    private Boolean error;
    private String message;
    private T data;
}
