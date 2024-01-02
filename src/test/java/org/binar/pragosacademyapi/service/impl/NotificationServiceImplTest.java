package org.binar.pragosacademyapi.service.impl;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.aspectj.lang.annotation.Before;
import org.binar.pragosacademyapi.entity.Notification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

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

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testSendNotification() {

    }

    @Test
    public void testSendNotificationList() {

    }
}
