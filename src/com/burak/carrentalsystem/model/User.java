package com.burak.carrentalsystem.model;

import java.io.Serializable;
import java.util.UUID;

public class User implements Serializable {

    private String id;
    private String username;
    private String fullName;
    private String password;
    private String email;
    private String phone;
    private String address;
    private Role role;

    public User(String username, String fullName, String password, String email, String phone,
            String address, Role role) {
        this.id = UUID.randomUUID().toString();
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.role = role;
    }

    // --- ГЕТТЕРИ ---
    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public Role getRole() {
        return role;
    }

    public boolean isAdmin() {
        return role == Role.ADMIN;
    }

    // --- СЕТЕРИ ---

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    // 🔥 ВАЖЛИВО: ДОДАЙ ЦЕЙ МЕТОД, ЩОБ МІНЯТИ РОЛЬ
    public void setRole(Role role) {
        this.role = role;
    }

    @Override
    public String toString() {
        String roleIcon = (role == Role.ADMIN) ? "🛡️ ADMIN" : "👤 CUSTOMER";
        return String.format("[%s] %s (%s) | 📧 %s", roleIcon, username, fullName, email);
    }
}