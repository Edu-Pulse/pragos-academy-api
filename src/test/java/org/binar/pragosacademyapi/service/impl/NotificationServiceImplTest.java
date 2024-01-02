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

import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
public class NotificationServiceImplTest {
    @Mock
    private ExecutorService executorService;

    @Mock
    private FirebaseDatabase firebaseDatabase;

    @Mock
    private DatabaseReference databaseReference;

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Test
    void testSendNotification_singleUser() {
        String email = "test@example.com";
        String message = "This is a test notification";
        notificationService.sendNotification(email, message);
        verify(executorService).submit(any(Runnable.class));
        verify(firebaseDatabase).getReference(anyString());
        verify(databaseReference).push();
        verify(databaseReference).setValueAsync(any(Notification.class));
    }

    @Test
    void testSendNotification_multipleUsers() {
        List<String> usersEmail = List.of("user1@example.com", "user2@example.com");
        String message = "This is a message for multiple users";
        notificationService.sendNotification(usersEmail, message);
        verify(executorService).submit(any(Runnable.class));
        verify(firebaseDatabase, times(2)).getReference(anyString());
        verify(databaseReference, times(2)).push();
        verify(databaseReference, times(2)).setValueAsync(any(Notification.class));
    }
}
