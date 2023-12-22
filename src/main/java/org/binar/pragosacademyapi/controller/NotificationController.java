package org.binar.pragosacademyapi.controller;

import org.binar.pragosacademyapi.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NotificationController {

    @Autowired
    private NotificationService notificationService;

    @PostMapping(
            value = "/notif/{id}",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public void sendNotification(@PathVariable Long id, @RequestBody String message){
        notificationService.sendNotification(id, message);
    }
}
