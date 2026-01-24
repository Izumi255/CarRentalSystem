package com.burak.carrentalsystem;


import com.burak.carrentalsystem.dto.UserStoreDto;
import com.burak.carrentalsystem.service.UserService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        UserService userService = new UserService();

        System.out.println("👋 ВІТАЄМО В CAR RENTAL SYSTEM");
        System.out.println("---------------------------------");

        // --- КРОК 1: РЕЄСТРАЦІЯ ---
        System.out.println("📝 Давайте зареєструємось!");

        System.out.print("Введіть логін: ");
        String login = scanner.nextLine();

        System.out.print("Введіть ім'я: ");
        String name = scanner.nextLine();

        System.out.print("Введіть email: ");
        String email = scanner.nextLine();

        System.out.print("Введіть пароль: ");
        String password = scanner.nextLine();

        System.out.print("Введіть телефон: ");
        String phone = scanner.nextLine();

        System.out.print("Введіть адресу: ");
        String address = scanner.nextLine();

        try {
            // Пакуємо дані в DTO
            UserStoreDto userDto = new UserStoreDto(login, name, email, password, phone, address);

            // Викликаємо наш сервіс (тут працює вся логіка Дня 4)
            userService.registerUser(userDto);
            System.out.println("\n✅ Успішно! Ви зареєстровані.");

        } catch (IllegalArgumentException e) {
            System.out.println("\n❌ Помилка реєстрації: " + e.getMessage());
            return; // Зупиняємо програму, якщо помилка
        }

        // --- КРОК 2: ВХІД ---
        System.out.println("\n🔑 А тепер спробуйте увійти в систему.");

        System.out.print("Логін: ");
        String inputLogin = scanner.nextLine();

        System.out.print("Пароль: ");
        String inputPass = scanner.nextLine();

        try {
            userService.login(inputLogin, inputPass);
            System.out.println("🎉 УРА! Ви увійшли в систему як " + inputLogin);
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }
}