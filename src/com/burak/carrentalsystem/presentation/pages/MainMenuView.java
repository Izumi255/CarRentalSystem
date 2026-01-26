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
            System.out.println("\n=========================================");
            ConsoleColors.print(ConsoleColors.PURPLE,
                    "👤 Ви увійшли як: " + user.getUsername() + " (" + user.getRole() + ")");
            System.out.println("=========================================");

            // ЗАГАЛЬНЕ ДЛЯ ВСІХ
            ConsoleColors.print(ConsoleColors.YELLOW, "[1] 🚗 Переглянути доступні авто");
            ConsoleColors.print(ConsoleColors.YELLOW, "[2] 👤 Мій профіль");

            // ТІЛЬКИ ДЛЯ АДМІНІВ (Пункт 4 вимог)
            if (user.getRole() == Role.ADMIN) {
                ConsoleColors.print(ConsoleColors.CYAN, "--- АДМІН ПАНЕЛЬ ---");
                ConsoleColors.print(ConsoleColors.CYAN, "[3] ➕ Додати нове авто");
                ConsoleColors.print(ConsoleColors.CYAN, "[4] 📊 Звіт (Excel/CSV)");
                ConsoleColors.print(ConsoleColors.CYAN, "[5] 👥 Усі користувачі");
            }

            ConsoleColors.print(ConsoleColors.RED, "[0] 🚪 Вихід");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    new CarListView().show();
                    break;
                case "2":
                    System.out.println(user); // Використовує toString() з User
                    break;
                case "3":
                    if (user.getRole() == Role.ADMIN) {
                        new AddCarForm().show();
                    } else {
                        ConsoleColors.print(ConsoleColors.RED, "⛔ У вас немає прав!");
                    }
                    break;
                case "4":
                    if (user.getRole() == Role.ADMIN) {
                        new ReportService().exportUsersToExcel();
                    } else {
                        ConsoleColors.print(ConsoleColors.RED, "⛔ У вас немає прав!");
                    }
                    break;
                case "0":
                    SessionManager.clearSession(); // 🗑️ Видаляємо сесію
                    ConsoleColors.print(ConsoleColors.BLUE, "Ви вийшли з аккаунту.");
                    return; // Повертає нас в AuthView (до вибору Вхід/Реєстрація)
            }
        }
    }
}