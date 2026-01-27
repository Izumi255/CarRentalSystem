package com.burak.carrentalsystem.presentation.utils;

import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.service.UserService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class SessionManager {

    private static final String SESSION_FILE = "data/session.txt";

    public static void saveSession(User user) {
        try {
            Files.writeString(Paths.get(SESSION_FILE), user.getUsername());
        } catch (IOException e) {
            System.err.println("⚠️ Не вдалося зберегти сесію.");
        }
    }

    public static void clearSession() {
        try {
            Files.deleteIfExists(Paths.get(SESSION_FILE));
        } catch (IOException e) {
            // ігноруємо
        }
    }

    public static User restoreSession(UserService userService) {
        Path path = Paths.get(SESSION_FILE);
        if (Files.exists(path)) {
            try {
                String username = Files.readString(path).trim();
                if (!username.isEmpty()) {

                    return userService.findByUsername(username).orElse(null);
                }
            } catch (IOException e) {
                return null;
            }
        }
        return null;
    }
}