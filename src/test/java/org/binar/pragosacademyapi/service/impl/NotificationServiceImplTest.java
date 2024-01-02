package org.binar.pragosacademyapi.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.binar.pragosacademyapi.entity.Notification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificationServiceImplTest {
    @Mock
    private ExecutorService executorService;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testSendNotification() {
        String email = "test@example.com";
        String message = "Hello, this is a notification message.";
        notificationService.sendNotification(email, message);
        verify(executorService).submit(any(Runnable.class));
    }

    @Test
    void testSendNotificationToList() {
        String message = "Hello, this is a notification message.";
        String email1 = "user1@example.com";
        String email2 = "user2@example.com";
        notificationService.sendNotification(Arrays.asList(email1, email2), message);
        verify(executorService).submit(any(Runnable.class));
    }
}
