package com.burak.CarRentalSystem.repository;

import com.burak.CarRentalSystem.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FileUserRepository implements CrudRepository<User> {

    private static final String FILE_PATH = "users.json";
    private final Gson gson;
    private List<User> users; // Це наш кеш (список у пам'яті)

    public FileUserRepository() {
        // Налаштовуємо Gson (красивий вивід JSON)
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.users = new ArrayList<>();
        loadFromFile(); // При створенні одразу читаємо файл
    }

    // --- РЕАЛІЗАЦІЯ CRUD (Методи з інтерфейсу) ---

    @Override
    public void add(User user) {
        users.add(user);
        saveToFile(); // Змінили -> Зберегли
    }

    @Override
    public Optional<User> getById(String id) {
        // Шукаємо юзера в списку (Java Stream API)
        return users.stream()
                .filter(user -> user.getUsername().equals(id)) // У нас ID це поки username, або можна додати поле id
                // ПРИМІТКА: Якщо у User є поле 'id', краще шукати по ньому.
                // Але поки у нас username унікальний, можна так.
                .findFirst();
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users); // Повертаємо копію списку
    }

    @Override
    public void update(User updatedUser) {
        // Знаходимо старого юзера і замінюємо
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(updatedUser.getUsername())) {
                users.set(i, updatedUser);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public boolean delete(String id) {
        // Видаляємо, якщо знаходимо
        boolean removed = users.removeIf(user -> user.getUsername().equals(id));
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    // --- ДОДАТКОВІ МЕТОДИ (Пошук/Фільтрація) ---

    // Пошук по Email (Вимога Дня 3)
    public Optional<User> getByEmail(String email) {
        return users.stream()
                .filter(u -> u.getEmail().equalsIgnoreCase(email))
                .findFirst();
    }

    // --- РОБОТА З ФАЙЛОМ (Приватні методи) ---

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(users, writer);
        } catch (IOException e) {
            System.err.println("❌ Помилка збереження users.json: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return; // Якщо файлу немає, просто починаємо з порожнім списком
        }

        try (Reader reader = new FileReader(file)) {
            // Магія Gson: перетворюємо JSON назад у List<User>
            Type listType = new TypeToken<ArrayList<User>>(){}.getType();
            users = gson.fromJson(reader, listType);

            if (users == null) users = new ArrayList<>();

        } catch (IOException e) {
            System.err.println("❌ Помилка читання users.json: " + e.getMessage());
        }
    }
}