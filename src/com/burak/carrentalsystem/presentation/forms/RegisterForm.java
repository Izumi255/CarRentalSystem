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
        ConsoleColors.clearScreen();

        // --- ШАПКА ФОРМИ ---
        System.out.println(ConsoleColors.CYAN_BOLD +
                ConsoleColors.BOX_TOP_LEFT + "══════════════════════════════════════════════"
                + ConsoleColors.BOX_TOP_RIGHT);
        System.out.println(
                ConsoleColors.BOX_VERTICAL + "          📝 НОВА РЕЄСТРАЦІЯ КОРИСТУВАЧА      "
                        + ConsoleColors.BOX_VERTICAL);
        System.out.println(
                ConsoleColors.BOX_BOTTOM_LEFT + "══════════════════════════════════════════════"
                        + ConsoleColors.BOX_BOTTOM_RIGHT +
                        ConsoleColors.RESET);

        System.out.println(ConsoleColors.CYAN + " Будь ласка, заповніть анкету нижче:\n"
                + ConsoleColors.RESET);

        System.out.print(ConsoleColors.GREEN_BOLD + "  " + ConsoleColors.USER_ICON + "  Логін:    "
                + ConsoleColors.RESET + ConsoleColors.ARROW + " ");
        String username = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  📛  Ім'я:     " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String fullName = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  📧  Email:    " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String email = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  " + ConsoleColors.KEY_ICON + "  Пароль:   "
                + ConsoleColors.RESET + ConsoleColors.ARROW + " ");
        String password = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  📱  Телефон:  " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String phone = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  🏠  Адреса:   " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String address = scanner.nextLine();

        System.out.println(ConsoleColors.CYAN + "\n  ──────────────────────────────────────────"
                + ConsoleColors.RESET);

        // 🔥 1. ВЕРИФІКАЦІЯ EMAIL
        String code = String.valueOf(new Random().nextInt(9000) + 1000);

        System.out.println(ConsoleColors.YELLOW + "  ⏳  З'єднання з сервером...");
        System.out.println(
                "  📤  Відправка коду підтвердження на " + ConsoleColors.WHITE_BOLD + email + "..."
                        + ConsoleColors.RESET);

        try {
            userService.sendVerificationCode(email, code);
            // Емуляція затримки для реалістичності
            Thread.sleep(1000);
        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n  " + ConsoleColors.ERROR_ICON
                            + " Помилка: Не вдалося відправити email. Реєстрація перервана.");
            pressEnterToContinue();
            return null;
        }

        System.out.println(
                ConsoleColors.GREEN + "  " + ConsoleColors.CHECK_ICON + "  Лист успішно надіслано!"
                        + ConsoleColors.RESET);
        System.out.println();

        ConsoleColors.printInline(ConsoleColors.YELLOW_BOLD,
                "  📩  Введіть код з листа " + ConsoleColors.ARROW + " ");
        String inputCode = scanner.nextLine();

        if (!inputCode.equals(code)) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n  " + ConsoleColors.ERROR_ICON + " Невірний код! Спробуйте ще раз.");
            pressEnterToContinue();
            return null;
        }

        try {
            UserStoreDto userDto = new UserStoreDto(username, fullName, email, password, phone,
                    address);
            userService.registerUser(userDto);

            User user = userService.findByUsername(username).orElseThrow();
            SessionManager.saveSession(user);

            ConsoleColors.print(ConsoleColors.GREEN_BOLD, "\n  " + ConsoleColors.CHECK_ICON
                    + "  Акаунт успішно створено! Ласкаво просимо.");
            Thread.sleep(1500); // Даємо час прочитати перед переходом в меню

            return user;

        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n  " + ConsoleColors.ERROR_ICON + " Критична помилка: " + e.getMessage());
            pressEnterToContinue();
            return null;
        }
    }

    private void pressEnterToContinue() {
        System.out.println(ConsoleColors.RESET + "\nНатисніть Enter, щоб продовжити...");
        scanner.nextLine();
    }
}