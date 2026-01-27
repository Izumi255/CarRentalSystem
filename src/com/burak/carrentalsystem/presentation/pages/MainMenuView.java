package com.burak.carrentalsystem.presentation.pages;

import com.burak.carrentalsystem.model.Role;
import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.presentation.forms.AddCarForm;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.presentation.utils.SessionManager;
import com.burak.carrentalsystem.service.ReportService;

import java.util.Scanner;

public class MainMenuView {

    private final User user;
    private final Scanner scanner;

    public MainMenuView(User user) {
        this.user = user;
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        while (true) {
            ConsoleColors.clearScreen();

            // --- ВЕРХНЯ ПАНЕЛЬ КОРИСТУВАЧА ---
            printUserHeader();

            System.out.println(ConsoleColors.CYAN + " Оберіть дію:" + ConsoleColors.RESET);

            // --- ЗАГАЛЬНЕ МЕНЮ ---
            System.out.println("  [1] " + ConsoleColors.CAR_ICON + "  Переглянути доступні авто");
            System.out.println("  [2] " + ConsoleColors.USER_ICON + "  Мій профіль");

            // --- АДМІН ПАНЕЛЬ (Тільки якщо адмін) ---
            if (user.getRole() == Role.ADMIN) {
                System.out.println(ConsoleColors.PURPLE + "\n  ─── 🛠 АДМІНІСТРУВАННЯ ───"
                        + ConsoleColors.RESET);
                System.out.println(ConsoleColors.PURPLE + "  [3] ➕ Додати нове авто");
                System.out.println(ConsoleColors.PURPLE + "  [4] 📊 Згенерувати звіт (Excel/CSV)");
                System.out.println(ConsoleColors.PURPLE + "  [5] 👥 Усі користувачі");
            }

            System.out.println(
                    ConsoleColors.BLUE + "\n  ──────────────────────────" + ConsoleColors.RESET);
            System.out.println(ConsoleColors.RED + "  [0] 🚪 Вихід з акаунту");

            System.out.println();
            ConsoleColors.printInline(ConsoleColors.YELLOW_BOLD,
                    " " + ConsoleColors.ARROW + " Ваш вибір > ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    new CarListView().show();
                    break;
                case "2":
                    showUserProfile();
                    break;
                case "3":
                    if (isAdmin()) {
                        new AddCarForm().show();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case "4":
                    if (isAdmin()) {
                        System.out.println(ConsoleColors.YELLOW + "⏳ Генерація звіту...");
                        new ReportService().exportUsersToExcel();
                        pressEnterToContinue();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case "5":
                    if (isAdmin()) {
                        ConsoleColors.print(ConsoleColors.YELLOW,
                                "🚧 Цей функціонал ще в розробці...");
                        pressEnterToContinue();
                    } else {
                        showAccessDenied();
                    }
                    break;
                case "0":
                    SessionManager.clearSession();
                    // ✅ ВИПРАВЛЕНО: getFullName() замість getFirstName()
                    ConsoleColors.print(ConsoleColors.BLUE,
                            "\n👋 До побачення, " + user.getFullName() + "!");
                    try {
                        Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                    return;
                default:
                    ConsoleColors.print(ConsoleColors.RED, "⚠️ Невідома команда.");
                    try {
                        Thread.sleep(800);
                    } catch (Exception e) {
                    }
            }
        }
    }

    // --- ДОПОМІЖНІ МЕТОДИ ---

    private boolean isAdmin() {
        return user.getRole() == Role.ADMIN;
    }

    private void showAccessDenied() {
        ConsoleColors.print(ConsoleColors.RED_BOLD, "⛔ У вас немає прав адміністратора!");
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
    }

    private void printUserHeader() {
        String roleColor =
                (user.getRole() == Role.ADMIN) ? ConsoleColors.RED_BOLD : ConsoleColors.GREEN_BOLD;
        String roleName = (user.getRole() == Role.ADMIN) ? "ADMINISTRATOR" : "USER";

        // Верхня рамка
        System.out.println(ConsoleColors.BLUE_BOLD +
                ConsoleColors.BOX_TOP_LEFT + "═════════════════════════════════════════════"
                + ConsoleColors.BOX_TOP_RIGHT + ConsoleColors.RESET);

        // Рядок 1: Ім'я (Зменшили до 22)
        System.out.println(
                ConsoleColors.BLUE_BOLD + ConsoleColors.BOX_VERTICAL + ConsoleColors.RESET +
                        "  👤 Ви увійшли як: " + ConsoleColors.WHITE_BOLD + user.getFullName()
                        + ConsoleColors.RESET +
                        padRight(25 - user.getFullName().length()) + ConsoleColors.BLUE_BOLD
                        + ConsoleColors.BOX_VERTICAL + ConsoleColors.RESET);

        // Рядок 2: Роль (Зменшили до 32)
        System.out.println(
                ConsoleColors.BLUE_BOLD + ConsoleColors.BOX_VERTICAL + ConsoleColors.RESET +
                        "  🛡️ Роль: " + roleColor + roleName + ConsoleColors.RESET +
                        padRight(34 - roleName.length()) + ConsoleColors.BLUE_BOLD
                        + ConsoleColors.BOX_VERTICAL + ConsoleColors.RESET);

        // Нижня рамка
        System.out.println(ConsoleColors.BLUE_BOLD +
                ConsoleColors.BOX_BOTTOM_LEFT + "═════════════════════════════════════════════"
                + ConsoleColors.BOX_BOTTOM_RIGHT + ConsoleColors.RESET);
    }

    private void showUserProfile() {
        ConsoleColors.clearScreen();
        System.out.println(
                ConsoleColors.CYAN_BOLD + "\n👤 --- ВАШ ПРОФІЛЬ ---" + ConsoleColors.RESET);
        System.out.println("🆔 Логін:   " + user.getUsername());
        System.out.println("📛 Ім'я:    " + user.getFullName());
        System.out.println("📧 Email:   " + user.getEmail());
        System.out.println("📱 Телефон: " + user.getPhone());
        System.out.println("🏠 Адреса:  " + user.getAddress());
        System.out.println("🛡️ Статус:  " + user.getRole());

        System.out.println(ConsoleColors.CYAN + "-----------------------" + ConsoleColors.RESET);
        pressEnterToContinue();
    }

    private void pressEnterToContinue() {
        System.out.println(ConsoleColors.RESET + "\nНатисніть Enter, щоб повернутись в меню...");
        scanner.nextLine();
    }

    private String padRight(int n) {
        if (n <= 0) {
            return "";
        }
        return String.format("%" + n + "s", "");
    }
}