package com.burak.carrentalsystem.model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

public class User {
    private String id;
    private String username;
    private String fullName;
    private String passwordHash;
    private String email;
    private String phone;
    private String address;
    private Role role;

    public User(String username, String fullName, String password, String email, String phone, String address, Role role) {
        this.id = UUID.randomUUID().toString();

        setUsername(username);
        setPassword(password);
        setEmail(email);

        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }


    public void setUsername(String username) {
        if (username == null || username.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Логін не може бути пустим!");
        }
        if (username.length() < 3) {
            throw new IllegalArgumentException("❌ Логін має бути мінімум 3 символи!");
        }
        this.username = username;
    }


    public void setPassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Пароль не може бути пустим!");
        }
        if (password.length() < 3) {
            throw new IllegalArgumentException("❌ Пароль надто слабкий! Мінімум 3 символи.");
        }

        this.passwordHash = hashString(password);
    }


    private String hashString(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Помилка хешування: " + e.getMessage());
        }
    }

    public void setEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Email не може бути пустим!");
        }
        if (!email.contains("@") || !email.contains(".")) {
            throw new IllegalArgumentException("❌ Некоректний Email (має бути @ та .)");
        }
        this.email = email;
    }

    public void setRole(Role role) { this.role = role; }

    // Геттери
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }
    public boolean isAdmin() { return role == Role.ADMIN; }

    @Override
    public String toString() {
        String roleIcon = (role == Role.ADMIN) ? "🛡️ ADMIN" : "👤 CUSTOMER";
        return String.format("[%s] %s (%s) | 📧 %s", roleIcon, username, fullName, email);
    }
}