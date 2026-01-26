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

    // Тепер метод повертає User (для авто-входу)
    public User show() {
        ConsoleColors.print(ConsoleColors.CYAN, "\n📝 --- РЕЄСТРАЦІЯ ---");

        System.out.print("Логін: ");
        String username = scanner.nextLine();
        // (Тут можна додати перевірку, чи вільний логін, перед заповненням анкети)

        System.out.print("Ім'я: ");
        String fullName = scanner.nextLine();
        System.out.print("Email: ");
        String email = scanner.nextLine();
        System.out.print("Пароль: ");
        String password = scanner.nextLine();
        System.out.print("Телефон: ");
        String phone = scanner.nextLine();
        System.out.print("Адреса: ");
        String address = scanner.nextLine();

        // 🔥 1. ВЕРИФІКАЦІЯ EMAIL
        String code = String.valueOf(
                new Random().nextInt(9000) + 1000); // Генеруємо 4 цифри (1000-9999)
        System.out.println("⏳ Відправляємо код на " + email + "...");

        try {
            userService.sendVerificationCode(email, code); // Відправка
        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED,
                    "❌ Не вдалося відправити email. Реєстрація перервана.");
            return null;
        }

        System.out.print(ConsoleColors.YELLOW + "📩 Введіть код з листа: " + ConsoleColors.RESET);
        String inputCode = scanner.nextLine();

        if (!inputCode.equals(code)) {
            ConsoleColors.print(ConsoleColors.RED, "❌ Невірний код! Спробуйте ще раз.");
            return null;
        }

        // 🔥 2. РЕЄСТРАЦІЯ (Якщо код правильний)
        try {
            UserStoreDto userDto = new UserStoreDto(username, fullName, email, password, phone,
                    address);
            userService.registerUser(userDto);

            // Дістаємо створеного юзера, щоб залогінити його
            User user = userService.findByUsername(username).orElseThrow();

            // Зберігаємо сесію (щоб не виходило з аккаунту)
            SessionManager.saveSession(user);

            return user; // Повертаємо юзера для авто-входу

        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED, "❌ Помилка: " + e.getMessage());
            return null;
        }
    }
}