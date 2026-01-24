package com.burak.carrentalsystem.dto;

import java.util.regex.Pattern;


public class UserStoreDto {

    // "Immutable" - поля final, змінити не можна
    private final String username;
    private final String fullName;
    private final String email;
    private final String password;
    private final String phone;
    private final String address;

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

    public UserStoreDto(String username, String fullName, String email, String password,
            String phone, String address) {

        if (username == null || username.trim().length() < 3) {
            throw new IllegalArgumentException("❌ Логін має бути мінімум 3 символи!");
        }
        if (email == null || !EMAIL_PATTERN.matcher(email).matches()) {
            throw new IllegalArgumentException("❌ Некоректний формат Email!");
        }
        if (password == null || password.length() < 4) {
            throw new IllegalArgumentException("❌ Пароль надто короткий (мінімум 4 символи)!");
        }
        if (fullName == null || fullName.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ ПІБ не може бути пустим!");
        }

        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

    // Тільки Геттери
    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }
}