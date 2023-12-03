package org.binar.pragosacademyapi.entity;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class Notification {
    private String sender;
    private Long receiver;
    private String text;
    private LocalDateTime dateTime;
}
