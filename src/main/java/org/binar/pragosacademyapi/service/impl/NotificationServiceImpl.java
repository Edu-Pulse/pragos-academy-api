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
    public void sendNotification(String email, String message) {
        executorService.submit(() -> {
            try {
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference(collectionName);

                String[] localDateTime = LocalDateTime.now().toString().split("T");
                String[] time = localDateTime[1].split("\\.");

                DatabaseReference newNotification = reference.push();
                Notification notification = Notification.builder()
                        .sender(appName)
                        .receiver(email)
                        .text(message)
                        .dateTime(localDateTime[0]+" "+ time[0])
                        .build();
                newNotification.setValueAsync(notification);
                log.info(ResponseUtils.MESSAGE_SUCCESS_SEND_NOTIFICATION  + email);
            }catch (Exception e){
                log.error(e.getMessage());
            }
        });
    }
    @Override
    public void sendNotification(List<String> usersEmail, String message) {
        executorService.submit(() -> {
           try {
               FirebaseDatabase database = FirebaseDatabase.getInstance();
               DatabaseReference reference = database.getReference(collectionName);
               String[] localDateTime = LocalDateTime.now().toString().split("T");
               String[] time = localDateTime[1].split("\\.");
               usersEmail.forEach(email -> {
                   DatabaseReference newNotification = reference.push();
                   Notification notification = Notification.builder()
                           .sender(appName)
                           .receiver(email)
                           .text(message)
                           .dateTime(localDateTime[0]+" "+ time[0])
                           .build();
                   newNotification.setValueAsync(notification);
                   log.info("Success send notification to user with email "+ email);
               });
           }catch (Exception e){
               log.error(e.getMessage());
           }
        });
    }
}
