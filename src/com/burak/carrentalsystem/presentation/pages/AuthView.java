package com.burak.carrentalsystem.presentation.pages;

import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.presentation.forms.LoginForm;
import com.burak.carrentalsystem.presentation.forms.RegisterForm;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.presentation.utils.SessionManager;
import com.burak.carrentalsystem.service.UserService;

import java.util.Scanner;

public class AuthView {

    private final UserService userService;
    private final Scanner scanner;

    public AuthView() {
        this.userService = new UserService();
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        // 🔥 ПЕРЕВІРКА СЕСІЇ (Якщо юзер вже був, заходимо автоматично)
        User savedUser = SessionManager.restoreSession(userService);
        if (savedUser != null) {
            ConsoleColors.print(ConsoleColors.GREEN,
                    "👋 З поверненням, " + savedUser.getFullName() + "!");
            new MainMenuView(savedUser).show(); // Одразу в меню
        }

        // Якщо сесії немає — показуємо вхід/реєстрацію
        while (true) {
            ConsoleColors.clearScreen();
            System.out.println("\n#########################################");
            ConsoleColors.print(ConsoleColors.BLUE, "👋 ВІТАЄМО В CAR RENTAL SYSTEM");
            System.out.println("#########################################");
            ConsoleColors.print(ConsoleColors.YELLOW, "[1] 🔐 Вхід");
            ConsoleColors.print(ConsoleColors.YELLOW, "[2] 📝 Реєстрація");
            ConsoleColors.print(ConsoleColors.RED, "[0] ❌ Вихід з програми");
            System.out.print("Ваш вибір: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    User loggedInUser = new LoginForm(userService).show();
                    if (loggedInUser != null) {
                        SessionManager.saveSession(loggedInUser); // Зберігаємо сесію
                        new MainMenuView(loggedInUser).show();
                    }
                    break;
                case "2":
                    // Реєстрація тепер повертає юзера (авто-вхід)
                    User registeredUser = new RegisterForm(userService).show();
                    if (registeredUser != null) {
                        new MainMenuView(registeredUser).show();
                    }
                    break;
                case "0":
                    System.exit(0);
                    break;
                default:
                    ConsoleColors.print(ConsoleColors.RED, "Невідома команда.");
            }
        }
    }
}