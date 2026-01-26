package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.repository.CrudRepository;
import com.burak.carrentalsystem.repository.FileUserRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collection;

public class ReportService {

    private final CrudRepository<User> userRepository;

    public ReportService() {
        this.userRepository = new FileUserRepository();
    }

    public void exportUsersToExcel() {
        String fileName = "data/users_report.csv";
        Collection<User> users = userRepository.getAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            // 1. Заголовки таблиці
            writer.write("ID,Login,Full Name,Email,Phone,Role");
            writer.newLine();

            // 2. Дані користувачів
            for (User user : users) {
                String line = String.format("%s,%s,%s,%s,%s,%s",
                        user.getId(),
                        user.getUsername(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getPhone(),
                        user.getRole()
                );
                writer.write(line);
                writer.newLine();
            }

            System.out.println("📊 Звіт успішно експортовано у файл: " + fileName);
            System.out.println("   (Ви можете відкрити його в Excel)");

        } catch (IOException e) {
            System.err.println("❌ Помилка запису файлу: " + e.getMessage());
        }
    }
}