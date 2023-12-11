package org.binar.pragosacademyapi.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.Notification;
import org.binar.pragosacademyapi.service.NotificationService;
import org.binar.pragosacademyapi.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class NotificationServiceImpl implements NotificationService {
    @Value("${firebase.collection.name}")
    private String collectionName;
    @Value("${app.name}")
    private String appName;
    private final ExecutorService executorService;
    @Autowired
    public NotificationServiceImpl(ExecutorService executorService){
        this.executorService = executorService;
    }

    @Override
    public void sendNotification(Long userId, String message) {
        executorService.submit(() -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(collectionName);

            DatabaseReference newNotification = reference.push();
            Notification notification = Notification.builder()
                    .sender(appName)
                    .receiver(userId)
                    .text(message)
                    .dateTime(LocalDateTime.now())
                    .build();
            newNotification.setValueAsync(notification);
            log.info(ResponseUtils.MESSAGE_SUCCESS_SEND_NOTIFICATION  + userId);
        });
    }
    @Override
    public void sendNotification(List<Long> users, String message) {
        executorService.submit(() -> {
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           DatabaseReference reference = database.getReference(collectionName);

           users.forEach(id -> {
               DatabaseReference newNotification = reference.push();
               Notification notification = Notification.builder()
                       .sender(appName)
                       .receiver(id)
                       .text(message)
                       .dateTime(LocalDateTime.now())
                       .build();
               newNotification.setValueAsync(notification);
           });
        });
    }
}
