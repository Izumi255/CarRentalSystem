package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.dto.UserStoreDto;
import com.burak.carrentalsystem.dto.UserUpdateDto;
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

        this.notificationService = new EmailNotificationService();
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.getById(username);
    }

    public void sendVerificationCode(String email, String code) {
        System.out.println("📧 Відправка коду на: " + email);
        notificationService.sendNotification(
                email,
                "🔐 Код підтвердження CarRental",
                "Ваш код для реєстрації: " + code + "\nНікому не повідомляйте його."
        );
    }

    public void registerUser(UserStoreDto userDto) {
        System.out.println("ℹ️ Спроба реєстрації користувача: " + userDto.getUsername());

        Optional<User> existingUser = userRepository.getById(userDto.getUsername());
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException(
                    "❌ Помилка: Користувач з логіном '" + userDto.getUsername() + "' вже існує!");
        }

        String hashedPassword = Password.hash(userDto.getPassword()).withBcrypt().getResult();

        User newUser = new User(
                userDto.getUsername(),
                userDto.getFullName(),
                hashedPassword,
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getAddress(),
                Role.CUSTOMER
        );

        // Збереження в базу
        userRepository.add(newUser);
        System.out.println("✅ Користувач успішно збережений у базі.");

    }

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

    public void updateUser(String username, UserUpdateDto updateDto) {
        Optional<User> userOpt = userRepository.getById(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("❌ Користувача не знайдено!");
        }
        User user = userOpt.get();

        System.out.println("ℹ️ Оновлення профілю для: " + username);

        if (updateDto.getFullName() != null) {
            user.setFullName(updateDto.getFullName());
        }
        if (updateDto.getPhone() != null) {
            user.setPhone(updateDto.getPhone());
        }
        if (updateDto.getAddress() != null) {
            user.setAddress(updateDto.getAddress());
        }

        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            String newHash = Password.hash(updateDto.getPassword()).withBcrypt().getResult();
            user.setPassword(newHash);
            System.out.println("🔐 Пароль успішно змінено.");
        }

        userRepository.add(user);
        System.out.println("✅ Дані профілю оновлено!");
    }

    public java.util.Collection<User> getAllUsers() {
        return userRepository.getAll();
    }
}
