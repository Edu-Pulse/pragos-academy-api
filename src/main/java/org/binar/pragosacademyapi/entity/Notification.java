package org.binar.pragosacademyapi.entity;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Notification {
    private String sender;
    private String receiver;
    private String text;
    private String dateTime;
}
