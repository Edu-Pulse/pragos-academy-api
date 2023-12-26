package org.binar.pragosacademyapi.service;

import java.util.List;

public interface NotificationService {
    void sendNotification(String email, String message);
    void sendNotification(List<String> usersEmail, String message);
}
