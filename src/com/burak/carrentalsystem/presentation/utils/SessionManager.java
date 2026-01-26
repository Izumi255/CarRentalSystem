package com.burak.carrentalsystem.presentation.utils;

import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class SessionManager {

    private static final String SESSION_FILE = "data/session.txt";

    // Зберегти сесію (коли входимо)
    public static void saveSession(User user) {
        try {
            Files.writeString(Paths.get(SESSION_FILE), user.getUsername());
        } catch (IOException e) {
            System.err.println("⚠️ Не вдалося зберегти сесію.");
        }
    }

    // Очистити сесію (коли виходимо)
    public static void clearSession() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (IOException e) {
            // ігноруємо
        }
    }

    // Спробувати відновити вхід (коли запускаємо програму)
    public static User restoreSession(UserService userService) {
        Path path = Paths.get(SESSION_FILE);
        if (Files.exists(path)) {
            try {
                String username = Files.readString(path).trim();
                if (!username.isEmpty()) {
                    // Шукаємо юзера в базі по збереженому логіну
                    // Тут ми трохи хачимо: використовуємо login, але без пароля, бо ми вже довіряємо файлу сесії
                    // В ідеалі треба токен, але для лаби так піде.
                    return userService.findByUsername(username).orElse(null);
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }
}