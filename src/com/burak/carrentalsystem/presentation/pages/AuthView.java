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
            ConsoleColors.clearScreen();
            ConsoleColors.print(ConsoleColors.GREEN_BOLD,
                    "\n " + ConsoleColors.CHECK_ICON + " З поверненням, " + savedUser.getFullName()
                            + "!");
            // Невелика пауза для ефекту (опціонально)
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }

            new MainMenuView(savedUser).show(); // Одразу в меню
        }

        // Якщо сесії немає — показуємо гарне меню
        while (true) {
            ConsoleColors.clearScreen();

            System.out.println(ConsoleColors.BLUE_BOLD +
                    ConsoleColors.BOX_TOP_LEFT + "══════════════════════════════════════════════"
                    + ConsoleColors.BOX_TOP_RIGHT +
                    ConsoleColors.RESET);

            // Текст заголовку з іконками машин
            System.out.println(ConsoleColors.BLUE_BOLD +
                    ConsoleColors.BOX_VERTICAL + "     " + ConsoleColors.CAR_ICON
                    + "   ВІТАЄМО В CAR RENTAL SYSTEM   " + ConsoleColors.CAR_ICON + "     "
                    + ConsoleColors.BOX_VERTICAL +
                    ConsoleColors.RESET);

            // Нижня рамка шапки
            System.out.println(ConsoleColors.BLUE_BOLD +
                    ConsoleColors.BOX_BOTTOM_LEFT + "══════════════════════════════════════════════"
                    + ConsoleColors.BOX_BOTTOM_RIGHT +
                    ConsoleColors.RESET);

            System.out.println(); // Відступ

            // --- МЕНЮ (MENU OPTIONS) ---
            ConsoleColors.print(ConsoleColors.CYAN,
                    "    [1] " + ConsoleColors.KEY_ICON + "  Вхід у систему");
            ConsoleColors.print(ConsoleColors.CYAN,
                    "    [2] " + ConsoleColors.USER_ICON + "  Створити акаунт");

            System.out.println(ConsoleColors.BLUE + "    ──────────────────────────────────────────"
                    + ConsoleColors.RESET); // Розділювач

            ConsoleColors.print(ConsoleColors.RED, "    [0] " + ConsoleColors.BOX_TOP_RIGHT
                    + "  Вихід з програми"); // Використав куточок як іконку виходу

            System.out.println(); // Відступ перед вводом
            ConsoleColors.printInline(ConsoleColors.YELLOW_BOLD,
                    " " + ConsoleColors.ARROW + " Ваш вибір > ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    User loggedInUser = new LoginForm(userService).show();
                    if (loggedInUser != null) {
                        SessionManager.saveSession(loggedInUser);
                        new MainMenuView(loggedInUser).show();
                    }
                    break;
                case "2":
                    User registeredUser = new RegisterForm(userService).show();
                    if (registeredUser != null) {
                        new MainMenuView(registeredUser).show();
                    }
                    break;
                case "0":
                    ConsoleColors.print(ConsoleColors.RED_BOLD,
                            "\nДо зустрічі! " + ConsoleColors.CAR_ICON);
                    System.exit(0);
                    break;
                default:
                    // Гарне повідомлення про помилку
                    ConsoleColors.print(ConsoleColors.RED_BOLD, "\n " + ConsoleColors.ERROR_ICON
                            + " Невідома команда, спробуйте ще раз.");
                    try {
                        Thread.sleep(800);
                    } catch (InterruptedException e) {
                    } // Пауза щоб прочитати помилку
            }
        }
    }
}