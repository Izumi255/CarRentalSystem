package com.burak.carrentalsystem.dto;

public class UserUpdateDto {

    private final String fullName;
    private final String phone;
    private final String address;
    private final String password;

    public UserUpdateDto(String fullName, String phone, String address, String password) {
        if (password != null && !password.isEmpty()
                && password.length() < 6) {
            throw new IllegalArgumentException(
                    "❌ Новий пароль надто короткий (мінімум 6 символів)!");
        }

        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.password = password;
    }

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