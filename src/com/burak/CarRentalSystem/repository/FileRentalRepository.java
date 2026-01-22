package com.burak.CarRentalSystem.repository;

import com.burak.CarRentalSystem.model.Rental;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileRentalRepository implements CrudRepository<Rental> {

    private static final String FILE_PATH = "rentals.json";
    private final Gson gson;
    private List<Rental> rentals;

    public FileRentalRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.rentals = new ArrayList<>();
        loadFromFile();
    }

    // --- CRUD ---

    @Override
    public void add(Rental rental) {
        rentals.add(rental);
        saveToFile();
    }

    @Override
    public Optional<Rental> getById(String id) {
        // У Rental немає явного ID (якщо ти не додавав поле id),
        // тому тут поки повертаємо порожнє, або треба додати id в клас Rental.
        // Але для MVP нам вистачить просто списку.
        return Optional.empty();
    }

    @Override
    public List<Rental> getAll() {
        return new ArrayList<>(rentals);
    }

    @Override
    public void update(Rental entity) {
        // Оренди зазвичай не змінюють (це чек), тому можна залишити пустим
        saveToFile();
    }

    @Override
    public boolean delete(String id) {
        // Якщо треба буде видалити історію
        return false;
    }

    // --- СПЕЦИФІЧНИЙ ПОШУК ---

    // Знайти всі оренди конкретного користувача
    public List<Rental> getRentalsByUser(String username) {
        return rentals.stream()
                .filter(rental -> rental.getUser().getUsername().equals(username))
                .collect(Collectors.toList());
    }

    // --- ФАЙЛИ ---

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(rentals, writer);
        } catch (IOException e) {
            System.err.println("❌ Помилка збереження rentals.json: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Rental>>(){}.getType();
            rentals = gson.fromJson(reader, listType);
            if (rentals == null) rentals = new ArrayList<>();
        } catch (IOException e) {
            System.err.println("❌ Помилка читання rentals.json: " + e.getMessage());
        }
    }
}