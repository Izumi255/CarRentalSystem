package com.burak.carrentalsystem.dto;

public class UserStoreDto {

    private final String username;
    private final String fullName;
    private final String email;
    private final String password;
    private final String phone;
    private final String address;

    // ✅ Конструктор тепер простенький і не "кусається"
    public UserStoreDto(String username, String fullName, String email, String password,
            String phone, String address) {
        this.username = username;
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phone = phone;
        this.address = address;
    }

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