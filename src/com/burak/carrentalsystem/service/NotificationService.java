package com.burak.carrentalsystem.service;

public interface NotificationService {
    void sendNotification(String toEmail, String subject, String message);
}