package com.burak.carrentalsystem.service;

import io.github.cdimascio.dotenv.Dotenv;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.util.Properties;

public class EmailNotificationService implements NotificationService {

    private static final Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
    private static final String SENDER_EMAIL = dotenv.get("EMAIL_USER");
    private static final String SENDER_PASSWORD = dotenv.get("EMAIL_PASS");

    @Override
    public void sendNotification(String toEmail, String subject, String messageText) {
        if (SENDER_EMAIL == null || SENDER_PASSWORD == null) {
            System.err.println("❌ ПОМИЛКА: Не знайдено змінні в .env файлі!");
            return;
        }

        System.out.println("🔄 З'єднання з сервером Gmail...");

        // 1. Налаштування
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Фікс для SSL/TLS
        props.put("mail.smtp.ssl.protocols", "TLSv1.2");
        props.put("mail.smtp.ssl.trust", "smtp.gmail.com");

        // 2. Створення сесії
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
            }
        });

        try {
            // 3. Створення листа
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SENDER_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject(subject);
            message.setText(messageText);

            // 4. Відправка
            Transport.send(message);
            System.out.println("✅ ЛИСТ ВІДПРАВЛЕНО на: " + toEmail);

        } catch (MessagingException e) {
            System.err.println("❌ Помилка відправки пошти: " + e.getMessage());
            e.printStackTrace();
        }
    }
}