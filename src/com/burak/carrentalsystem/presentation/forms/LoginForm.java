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
        ConsoleColors.print(ConsoleColors.CYAN, "\n🔑 --- ВХІД У СИСТЕМУ ---");

        System.out.print("Логін: ");
        String username = scanner.nextLine();

        System.out.print("Пароль: ");
        String password = scanner.nextLine();

        try {
            return userService.login(username, password);
        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED, "❌ Помилка входу: " + e.getMessage());
            return null;
        }
    }
}