package com.burak.carrentalsystem.presentation.forms;

import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.service.UserService;

import java.util.Scanner;

public class LoginForm {

    private final UserService userService;
    private final Scanner scanner;

    public LoginForm(UserService userService) {
        this.userService = userService;
        this.scanner = new Scanner(System.in);
    }

    public User show() {
        ConsoleColors.clearScreen();

        System.out.println(ConsoleColors.CYAN_BOLD +
                ConsoleColors.BOX_TOP_LEFT + "══════════════════════════════════════════════"
                + ConsoleColors.BOX_TOP_RIGHT);
        System.out.println(
                ConsoleColors.BOX_VERTICAL + "           🔑 АВТОРИЗАЦІЯ В СИСТЕМІ           "
                        + ConsoleColors.BOX_VERTICAL);
        System.out.println(
                ConsoleColors.BOX_BOTTOM_LEFT + "══════════════════════════════════════════════"
                        + ConsoleColors.BOX_BOTTOM_RIGHT +
                        ConsoleColors.RESET);

        System.out.println(
                ConsoleColors.CYAN + " Введіть ваші дані для входу:\n" + ConsoleColors.RESET);

        // --- ВВЕДЕННЯ ДАНИХ ---
        System.out.print(ConsoleColors.GREEN_BOLD + "  " + ConsoleColors.USER_ICON + "  Логін:   "
                + ConsoleColors.RESET + ConsoleColors.ARROW + " ");
        String username = scanner.nextLine();

        System.out.print(ConsoleColors.GREEN_BOLD + "  " + ConsoleColors.KEY_ICON + "  Пароль:  "
                + ConsoleColors.RESET + ConsoleColors.ARROW + " ");
        String password = scanner.nextLine();

        System.out.println(ConsoleColors.CYAN + "  ──────────────────────────────────────────"
                + ConsoleColors.RESET);

        // --- ЛОГІКА ВХОДУ ---
        try {
            System.out.println(ConsoleColors.YELLOW + "  ⏳ Перевірка даних...");
            Thread.sleep(800);

            User user = userService.login(username, password);

            ConsoleColors.print(ConsoleColors.GREEN_BOLD,
                    "\n  " + ConsoleColors.CHECK_ICON + "  Успішний вхід! Ласкаво просимо, "
                            + user.getFullName() + ".");
            Thread.sleep(1200);

            return user;

        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n  " + ConsoleColors.ERROR_ICON + " Помилка входу: " + e.getMessage());
            System.out.println(ConsoleColors.RESET + "\nНатисніть Enter, щоб спробувати ще раз...");
            scanner.nextLine();
            return null;
        }
    }
}