package org.binar.pragosacademyapi.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import lombok.extern.slf4j.Slf4j;
import org.binar.pragosacademyapi.entity.Notification;
import org.binar.pragosacademyapi.service.NotificationService;
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
    private String COLLECTION_NAME;
    @Autowired
    private ExecutorService executorService;

    @Override
    public void sendNotification(Long userId, String message) {
        executorService.submit(() -> {
            FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference reference = database.getReference(COLLECTION_NAME);

            DatabaseReference newNotification = reference.push();
            Notification notification = Notification.builder()
                    .sender("Pragos Academy")
                    .receiver(userId)
                    .text(message)
                    .dateTime(LocalDateTime.now())
                    .build();
            newNotification.setValueAsync(notification);
            log.info("Success send notification to user with id "+ userId);
        });
    }
    @Override
    public void sendNotification(List<Long> users, String message) {
        executorService.submit(() -> {
           FirebaseDatabase database = FirebaseDatabase.getInstance();
           DatabaseReference reference = database.getReference(COLLECTION_NAME);

           users.forEach(id -> {
               DatabaseReference newNotification = reference.push();
               Notification notification = Notification.builder()
                       .sender("Pragos Academy")
                       .receiver(id)
                       .text(message)
                       .dateTime(LocalDateTime.now())
                       .build();
               newNotification.setValueAsync(notification);
           });
        });
    }
}
