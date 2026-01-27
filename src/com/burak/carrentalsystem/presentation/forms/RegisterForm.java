package com.burak.carrentalsystem.presentation.forms;

import com.burak.carrentalsystem.dto.UserStoreDto;
import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.presentation.utils.SessionManager;
import com.burak.carrentalsystem.service.UserService;

import java.util.Random;
import java.util.Scanner;

public class RegisterForm {

    private final UserService userService;
    private final Scanner scanner;

    public RegisterForm(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public User show() {
        String username, fullName, email, password, phone, address;

        ConsoleColors.clearScreen();
        printHeader();

        // 1. ЛОГІН (Перевірка на унікальність + мінімум 3 символи)
        while (true) {
            username = input("👤 Логін");
            if (username.length() < 3) {
                printError("Логін занадто короткий (мінімум 3 символи)!");
                continue;
            }
            if (userService.findByUsername(username).isPresent()) {
                printError("Цей логін вже зайнятий! Придумайте інший.");
                continue;
            }
            break; // Якщо дійшли сюди - все ок, виходимо з циклу
        }

        // 2. ІМ'Я
        while (true) {
            fullName = input("📛 ПІБ");
            if (fullName.length() < 2) {
                printError("Ім'я занадто коротке!");
                continue;
            }
            break;
        }

        // 3. EMAIL (МИТТЄВА ПЕРЕВІРКА НА ДУБЛІКАТ)
        while (true) {
            email = input("📧 Email");

            // Перевірка на пустоту
            if (email.length() < 5 || !email.contains("@")) {
                printError("Введіть коректний Email (наприклад, user@mail.com)");
                continue;
            }

            // Перевірка в базі
            String finalEmail = email;
            boolean exists = userService.getAllUsers().stream()
                    .anyMatch(u -> u.getEmail().equalsIgnoreCase(finalEmail));

            if (exists) {
                printError("Цей Email вже зареєстрований у системі!");
                continue;
            }
            break;
        }

        // 4. ПАРОЛЬ (Мінімум 6 символів)
        while (true) {
            password = input(ConsoleColors.KEY_ICON + " Пароль");
            if (password.length() < 6) {
                printError("Пароль має бути мінімум 6 символів!");
                continue;
            }
            break;
        }

        // 5. ТЕЛЕФОН
        while (true) {
            phone = input("📱 Телефон");
            if (!phone.startsWith("+421")) {
                printError("Номер має починатися з +421");
                continue;
            }
            if (phone.length() < 12 || phone.length() > 15) {
                printError("Невірна довжина номера (має бути 12-15 символів)!");
                continue;
            }
            break;
        }

        // 6. АДРЕСА
        while (true) {
            address = input("🏠 Адреса");
            if (address.length() < 3) {
                printError("Адреса занадто коротка!");
                continue;
            }
            break;
        }

        // --- ВЕРИФІКАЦІЯ ---
        System.out.println(ConsoleColors.CYAN + "\n  ──────────────────────────────────────────"
                + ConsoleColors.RESET);
        String code = String.valueOf(new Random().nextInt(9000) + 1000);

        System.out.println(ConsoleColors.YELLOW + "  ⏳  Відправка коду на " + email + "...");
        userService.sendVerificationCode(email, code);

        System.out.print(
                ConsoleColors.YELLOW_BOLD + "  📩  Код підтвердження ➜ " + ConsoleColors.RESET);
        String inputCode = scanner.nextLine().trim();

        if (!inputCode.equals(code)) {
            printError("Невірний код! Реєстрацію скасовано.");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ignored) {
            }
            return null;
        }

        // --- ЗБЕРЕЖЕННЯ ---
        // Тут ми вже не боїмося помилок, бо все перевірили вище!
        try {
            UserStoreDto userDto = new UserStoreDto(username, fullName, email, password, phone,
                    address);
            userService.registerUser(userDto);

            User user = userService.findByUsername(username).orElseThrow();
            SessionManager.saveSession(user);

            System.out.println(ConsoleColors.GREEN_BOLD + "\n  ✅ Акаунт успішно створено!"
                    + ConsoleColors.RESET);
            Thread.sleep(1500);
            return user;

        } catch (Exception e) {
            printError("Щось пішло не так: " + e.getMessage());
            return null;
        }
    }

    // --- ДОПОМІЖНІ МЕТОДИ ---
    private String input(String label) {
        System.out.print(
                ConsoleColors.GREEN_BOLD + "  " + label + ": " + " ".repeat(12 - label.length())
                        + ConsoleColors.RESET + ConsoleColors.ARROW + " ");
        String text = scanner.nextLine().trim();
        // Якщо хочеш кнопку виходу, розкоментуй це:
        // if (text.equals("0")) throw new RuntimeException("BACK");
        return text;
    }

    private void printError(String message) {
        System.out.println(ConsoleColors.RED_BOLD + "  ❌ " + message + ConsoleColors.RESET);
    }

    private void printHeader() {
        System.out.println(
                ConsoleColors.CYAN_BOLD + " ╔══════════════════════════════════════════════╗");
        System.out.println(" ║          📝 НОВА РЕЄСТРАЦІЯ КОРИСТУВАЧА      ║");
        System.out.println(
                " ╚══════════════════════════════════════════════╝" + ConsoleColors.RESET + "\n");
    }
}