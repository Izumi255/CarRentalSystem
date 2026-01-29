package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.dto.UserStoreDto;
import com.burak.carrentalsystem.dto.UserUpdateDto;
import com.burak.carrentalsystem.model.Role;
import com.burak.carrentalsystem.model.User;
import com.burak.carrentalsystem.repository.CrudRepository;
import com.burak.carrentalsystem.repository.FileUserRepository;
import com.password4j.Password;

import java.util.List;
import java.util.Optional;


public class UserService {

    private final CrudRepository<User> userRepository;
    private final NotificationService notificationService;

    public UserService() {
        this.userRepository = new FileUserRepository();
        this.notificationService = new EmailNotificationService();
    }

    // 1. Валідація даних
    public void validateUserData(String username, String email, String phone, String address) {
        if (userRepository.getById(username).isPresent()) {
            throw new IllegalArgumentException(
                    "❌ Користувач з логіном '" + username + "' вже існує!");
        }

        String cleanEmail = (email != null) ? email.trim() : "";
        boolean emailExists = userRepository.getAll().stream()
                .anyMatch(u -> u.getEmail().equalsIgnoreCase(cleanEmail));

        if (emailExists) {
            throw new IllegalArgumentException("❌ Email '" + cleanEmail + "' вже зареєстрований!");
        }

        validatePhone(phone);

        if (address == null || address.trim().length() < 3) {
            throw new IllegalArgumentException("❌ Адреса занадто коротка!");
        }
    }

    // 2. Пошук юзера
    public Optional<User> findByUsername(String username) {
        return userRepository.getById(username);
    }

    // 3. Відправка коду
    public void sendVerificationCode(String email, String code) {
        notificationService.sendNotification(
                email,
                "🔐 Код підтвердження CarRental",
                "Ваш код: " + code
        );
    }

    public void registerUser(UserStoreDto userDto) {
        validateUserData(userDto.getUsername(), userDto.getEmail(), userDto.getPhone(),
                userDto.getAddress());

        String hashedPassword = Password.hash(userDto.getPassword()).withBcrypt().getResult();

        User newUser = new User(
                userDto.getUsername(),
                userDto.getFullName(),
                hashedPassword,
                userDto.getEmail(),
                userDto.getPhone(),
                userDto.getAddress(),
                userDto.getRole()
        );

        userRepository.add(newUser);
    }

    // 5. Авторизація
    public User login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.getById(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("❌ Невірний логін!");
        }
        User user = userOpt.get();
        if (Password.check(rawPassword, user.getPassword()).withBcrypt()) {
            return user;
        } else {
            throw new IllegalArgumentException("❌ Невірний пароль!");
        }
    }

    // 6. Оновлення
    public void updateUser(String username, UserUpdateDto updateDto) {
        Optional<User> userOpt = userRepository.getById(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("❌ Користувача не знайдено!");
        }

        User user = userOpt.get();
        if (updateDto.getFullName() != null) {
            user.setFullName(updateDto.getFullName());
        }
        if (updateDto.getPhone() != null) {
            validatePhone(updateDto.getPhone());
            user.setPhone(updateDto.getPhone());
        }
        if (updateDto.getAddress() != null) {
            user.setAddress(updateDto.getAddress());
        }
        if (updateDto.getPassword() != null && !updateDto.getPassword().isEmpty()) {
            user.setPassword(Password.hash(updateDto.getPassword()).withBcrypt().getResult());
        }
        userRepository.add(user); // Перезаписуємо оновленого юзера
    }

    public List<User> getAllUsers() {
        return userRepository.getAll();
    }

    // Приватні методи
    private void validatePhone(String phone) {
        if (phone == null || !phone.trim().startsWith("+421")) {
            throw new IllegalArgumentException("❌ Телефон має починатися з +421!");
        }
        String cleanPhone = phone.trim();
        if (cleanPhone.length() < 12 || cleanPhone.length() > 15) {
            throw new IllegalArgumentException("❌ Невірна довжина номера (12-15 символів).");
        }
    }

    public void deleteUser(String username) {
        if (username.equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException("⛔ Не можна видалити головного адміністратора!");
        }
        boolean isDeleted = userRepository.delete(username);
        if (!isDeleted) {
            throw new IllegalArgumentException(
                    "❌ Користувача з логіном '" + username + "' не знайдено.");
        }
    }

    public void changeUserRole(String username, Role newRole) {
        if (username.equalsIgnoreCase("admin")) {
            throw new IllegalArgumentException(
                    "⛔ Не можна змінювати роль головного адміністратора!");
        }

        Optional<User> userOpt = userRepository.getById(username);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException(
                    "❌ Користувача з логіном '" + username + "' не знайдено.");
        }

        User user = userOpt.get();
        if (user.getRole() == newRole) {
            throw new IllegalArgumentException("⚠️ Користувач вже має цю роль!");
        }

        user.setRole(newRole);
        userRepository.add(user);
    }
}