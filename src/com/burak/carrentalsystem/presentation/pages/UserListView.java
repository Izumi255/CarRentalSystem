package com.burak.carrentalsystem.presentation.pages;

import com.burak.carrentalsystem.model.Role;
import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.service.UserService;

import java.util.List;
import java.util.Scanner;

public class UserListView {

    private final UserService userService;
    private final Scanner scanner;

    public UserListView() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        while (true) {
            ConsoleColors.clearScreen();
            List<User> users = userService.getAllUsers();

            // --- ШАПКА ---
            System.out.println(ConsoleColors.PURPLE_BOLD +
                    ConsoleColors.BOX_TOP_LEFT
                    + "══════════════════════════════════════════════════════════════════════════════"
                    + ConsoleColors.BOX_TOP_RIGHT);
            System.out.println(ConsoleColors.BOX_VERTICAL
                    + "                        👥 КЕРУВАННЯ КОРИСТУВАЧАМИ                            "
                    + ConsoleColors.BOX_VERTICAL);
            System.out.println(ConsoleColors.BOX_BOTTOM_LEFT
                    + "══════════════════════════════════════════════════════════════════════════════"
                    + ConsoleColors.BOX_BOTTOM_RIGHT + ConsoleColors.RESET);

            // --- ТАБЛИЦЯ ---
            if (users.isEmpty()) {
                System.out.println(ConsoleColors.YELLOW + "\n  🤷‍♂️ Список порожній.");
            } else {
                System.out.println();
                System.out.printf(ConsoleColors.CYAN_BOLD + "  %-12s  %-20s  %-25s  %-15s  %s%n"
                                + ConsoleColors.RESET,
                        "👤 ЛОГІН", "📛 ІМ'Я", "📧 EMAIL", "📱 ТЕЛЕФОН", "🔑 РОЛЬ");
                System.out.println(ConsoleColors.PURPLE
                        + "  ────────────  ────────────────────  ─────────────────────────  ───────────────  ──────────"
                        + ConsoleColors.RESET);

                for (User u : users) {
                    String name =
                            u.getFullName().length() > 19 ? u.getFullName().substring(0, 18) + "…"
                                    : u.getFullName();
                    String email = u.getEmail().length() > 24 ? u.getEmail().substring(0, 23) + "…"
                            : u.getEmail();
                    String roleColor = u.getRole() == Role.ADMIN ? ConsoleColors.RED_BOLD
                            : ConsoleColors.GREEN_BOLD;

                    System.out.printf("  %-12s  %-20s  %-25s  %-15s  %s%s%s%n",
                            u.getUsername(), name, email, u.getPhone(), roleColor, u.getRole(),
                            ConsoleColors.RESET
                    );
                }
                System.out.println(ConsoleColors.PURPLE
                        + "  ────────────────────────────────────────────────────────────────────────────────────────────"
                        + ConsoleColors.RESET);

                System.out.println(
                        ConsoleColors.CYAN + "  Всього: " + ConsoleColors.GREEN_BOLD + users.size()
                                + ConsoleColors.RESET);

                System.out.println("\n" + ConsoleColors.YELLOW + "  ["
                        + ConsoleColors.CYAN_BOLD + "логін" + ConsoleColors.YELLOW + "] "
                        // Жовті дужки, бірюзовий текст
                        + ConsoleColors.RED_BOLD + "💀 Напишіть логін, щоб ЗАБЛОКУВАТИ (видалити)"
                        + ConsoleColors.RESET);

                System.out.println(ConsoleColors.YELLOW + "  ["
                        + ConsoleColors.CYAN_BOLD + "0" + ConsoleColors.YELLOW + "]     "
                        + ConsoleColors.WHITE_BOLD + "🔙 Повернутися назад" + ConsoleColors.RESET);

                System.out.print(
                        ConsoleColors.YELLOW_BOLD + "\n  Ваш вибір ➜ " + ConsoleColors.RESET);

                String input = scanner.nextLine().trim();

                if (input.equals("0")) {
                    break;
                }

                // Спроба видалення
                try {
                    System.out.print(
                            ConsoleColors.RED + "  Ви впевнені, що хочете видалити '" + input
                                    + "'? (yes/no): " + ConsoleColors.RESET);
                    String confirm = scanner.nextLine().trim();

                    if (confirm.equalsIgnoreCase("yes") || confirm.equalsIgnoreCase("y")) {
                        userService.deleteUser(input);
                        System.out.println(
                                ConsoleColors.GREEN_BOLD + "  ✅ Користувача успішно видалено!"
                                        + ConsoleColors.RESET);
                        Thread.sleep(1500); // Пауза, щоб побачити результат
                    } else {
                        System.out.println(
                                ConsoleColors.YELLOW + "  Скасовано." + ConsoleColors.RESET);
                        Thread.sleep(800);
                    }
                } catch (Exception e) {
                    System.out.println(
                            ConsoleColors.RED_BOLD + "  " + e.getMessage() + ConsoleColors.RESET);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}