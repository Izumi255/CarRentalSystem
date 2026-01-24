package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.dto.UserStoreDto;
import com.burak.carrentalsystem.model.Role;
import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.repository.CrudRepository;
import com.burak.carrentalsystem.repository.FileUserRepository;
import com.password4j.Password;


import java.util.Optional;

public class UserService {

    private final CrudRepository<User> userRepository;
    private final NotificationService notificationService;

    public UserService() {
        this.userRepository = new FileUserRepository();
        // Використовуємо наш сервіс пошти (який читає .env)
        this.notificationService = new EmailNotificationService();
    }

    //РЕЄСТРАЦІЯ
    public void registerUser(UserStoreDto userDto) {
        System.out.println("ℹ️ Спроба реєстрації користувача: " + userDto.getUsername());

        // 1. Перевірка логіна
        Optional<User> existingUser = userRepository.getById(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(
                    "❌ Помилка: Користувач з логіном '" + userDto.getUsername() + "' вже існує!");
        }

        String hashedPassword = Password.hash(userDto.getPassword()).withBcrypt().getResult();

        // 3. Створення користувача
        User newUser = new User(
                userDto.getUsername(),
                userDto.getFullName(),
                hashedPassword,
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getAddress(),
                Role.CUSTOMER
        );

        // 4. Збереження в базу
        userRepository.add(newUser);
        System.out.println("✅ Користувач успішно збережений у базі.");

        // 5. Відправка листа
        notificationService.sendNotification(
                newUser.getEmail(),
                "Ласкаво просимо до CarRental!",
                "Вітаємо, " + newUser.getFullName() + "! Ви успішно зареєструвалися.\nВаш логін: "
                        + newUser.getUsername()
        );
    }

    //АУТЕНТИФІКАЦІЯ
    public User login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.getById(username);

        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("❌ Невірний логін або пароль!");
        }

        User user = userOpt.get();

        boolean isPasswordCorrect = Password.check(rawPassword, user.getPassword()).withBcrypt();

        if (isPasswordCorrect) {
            System.out.println("🔓 Вхід успішний: " + user.getUsername());
            return user;
        } else {
            throw new IllegalArgumentException("❌ Невірний логін або пароль!");
        }
    }
}