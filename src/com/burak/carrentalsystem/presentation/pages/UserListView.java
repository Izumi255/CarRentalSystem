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
                        + "  ────────────  ────────────────────  ─────────────────────────  ───────────────  ──────"
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
                        + "  ──────────────────────────────────────────────────────────────────────────────"
                        + ConsoleColors.RESET);
                System.out.println(
                        ConsoleColors.CYAN + "  Всього: " + ConsoleColors.GREEN_BOLD + users.size()
                                + ConsoleColors.RESET);
            }

            // --- МЕНЮ ДІЙ ---
            System.out.println("\n" + ConsoleColors.YELLOW + "  [" + ConsoleColors.CYAN_BOLD + "del"
                    + ConsoleColors.YELLOW + "]   "
                    + ConsoleColors.RED_BOLD + "💀 Видалити користувача" + ConsoleColors.RESET);

            // 🔥 НОВА КНОПКА
            System.out.println(ConsoleColors.YELLOW + "  [" + ConsoleColors.CYAN_BOLD + "role"
                    + ConsoleColors.YELLOW + "]  "
                    + ConsoleColors.BLUE_BOLD + "👑 Змінити роль (Admin/Customer)"
                    + ConsoleColors.RESET);

            System.out.println(ConsoleColors.YELLOW + "  [" + ConsoleColors.CYAN_BOLD + "0"
                    + ConsoleColors.YELLOW + "]     "
                    + ConsoleColors.WHITE_BOLD + "🔙 Назад" + ConsoleColors.RESET);

            System.out.print(ConsoleColors.YELLOW_BOLD + "\n  Ваш вибір ➜ " + ConsoleColors.RESET);
            String input = scanner.nextLine().trim();

            if (input.equals("0")) {
                break;
            }

            // --- ЛОГІКА ВИДАЛЕННЯ ---
            if (input.equalsIgnoreCase("del")) {
                System.out.print("Введіть логін для видалення: ");
                String login = scanner.nextLine().trim();
                try {
                    userService.deleteUser(login);
                    System.out.println(ConsoleColors.GREEN + "✅ Видалено!");
                    Thread.sleep(1000);
                } catch (Exception e) {
                    System.out.println(ConsoleColors.RED + "Помилка: " + e.getMessage());
                    try {
                        Thread.sleep(1500);
                    } catch (InterruptedException ignored) {
                    }
                }
            } else if (input.equalsIgnoreCase("role")) {
                System.out.println(); // Відступ
                System.out.print(
                        ConsoleColors.CYAN + "  Введіть логін користувача: " + ConsoleColors.RESET);
                String login = scanner.nextLine().trim();

                System.out.println(
                        ConsoleColors.YELLOW + "\n  Оберіть нову роль:" + ConsoleColors.RESET);
                System.out.println("  1. " + ConsoleColors.RED_BOLD + "ADMIN" + ConsoleColors.RESET
                        + " (Адміністратор)");
                System.out.println(
                        "  2. " + ConsoleColors.GREEN_BOLD + "CUSTOMER" + ConsoleColors.RESET
                                + " (Клієнт)");

                System.out.print(
                        ConsoleColors.YELLOW_BOLD + "  Ваш вибір ➜ " + ConsoleColors.RESET);
                String roleChoice = scanner.nextLine().trim();

                try {
                    if (roleChoice.equals("1")) {
                        userService.changeUserRole(login, Role.ADMIN);
                        System.out.println(ConsoleColors.GREEN_BOLD + "  ✅ Тепер "
                                + ConsoleColors.YELLOW_BOLD + login
                                + ConsoleColors.GREEN_BOLD + " — АДМІНІСТРАТОР!"
                                + ConsoleColors.RESET);
                    } else if (roleChoice.equals("2")) {
                        userService.changeUserRole(login, Role.CUSTOMER);
                        System.out.println(ConsoleColors.GREEN_BOLD + "  ✅ Тепер "
                                + ConsoleColors.YELLOW_BOLD + login
                                + ConsoleColors.GREEN_BOLD + " — звичайний клієнт."
                                + ConsoleColors.RESET);
                    } else {
                        System.out.println(ConsoleColors.RED + "  ❌ Невірний вибір ролі."
                                + ConsoleColors.RESET);
                    }
                    Thread.sleep(2000); // Пауза, щоб прочитати
                } catch (Exception e) {
                    System.out.println(ConsoleColors.RED_BOLD + "  ❌ Помилка: " + e.getMessage()
                            + ConsoleColors.RESET);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }
    }
}