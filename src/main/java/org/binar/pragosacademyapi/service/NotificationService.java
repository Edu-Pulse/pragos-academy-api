package org.binar.pragosacademyapi.service;

import java.util.List;

public interface NotificationService {
    void sendNotification(Long userId, String message);
    void sendNotification(List<Long> users, String message);
}
