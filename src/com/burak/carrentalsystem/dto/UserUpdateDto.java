package com.burak.carrentalsystem.dto;

public class UserUpdateDto {

    private final String fullName;
    private final String phone;
    private final String address;
    private final String password;

    public UserUpdateDto(String fullName, String phone, String address, String password) {
        if (fullName != null && fullName.trim().length() < 2) {
            throw new IllegalArgumentException("❌ Ім'я надто коротке!");
        }

        if (password != null && !password.isEmpty() && password.length() < 3) {
            throw new IllegalArgumentException(
                    "❌ Новий пароль надто короткий (мінімум 3 символи)!");
        }

        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

    // Геттери
    public String getFullName() {
        return fullName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getPassword() {
        return password;
    }
}