package com.burak.carrentalsystem.repository;

import com.burak.carrentalsystem.model.Car;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class FileCarRepository implements CrudRepository<Car> {

    private static final String FILE_PATH = "cars.json";
    private final Gson gson;
    private List<Car> cars;

    public FileCarRepository() {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.cars = new ArrayList<>();
        loadFromFile();
    }

    // --- CRUD ---

    @Override
    public void add(Car car) {
        cars.add(car);
        saveToFile();
    }

    @Override
    public Optional<Car> getById(String id) {
        return cars.stream()
                .filter(car -> car.getId().equals(id))
                .findFirst();
    }

    @Override
    public List<Car> getAll() {
        return new ArrayList<>(cars);
    }

    @Override
    public void update(Car updatedCar) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getId().equals(updatedCar.getId())) {
                cars.set(i, updatedCar);
                saveToFile();
                return;
            }
        }
    }

    @Override
    public boolean delete(String id) {
        boolean removed = cars.removeIf(car -> car.getId().equals(id));
        if (removed) {
            saveToFile();
        }
        return removed;
    }

    // --- 🔍 ПОШУК І ФІЛЬТРАЦІЯ (Виправлено під твій Car.java) ---

    // Знайти всі машини певного бренду (використовуємо getBrand)
    public List<Car> searchByBrand(String brand) {
        return cars.stream()
                .filter(car -> car.getBrand().equalsIgnoreCase(brand))
                .collect(Collectors.toList());
    }

    public List<Car> filterByPrice(double maxPrice) {
        return cars.stream()
                .filter(car -> car.getPricePerHour() <= maxPrice)
                .collect(Collectors.toList());
    }

    // --- ФАЙЛИ ---

    private void saveToFile() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(cars, writer);
        } catch (IOException e) {
            System.err.println("❌ Помилка збереження cars.json: " + e.getMessage());
        }
    }

    private void loadFromFile() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            Type listType = new TypeToken<ArrayList<Car>>() {
            }.getType();
            cars = gson.fromJson(reader, listType);
            if (cars == null) {
                cars = new ArrayList<>();
            }
        } catch (IOException e) {
            System.err.println("❌ Помилка читання cars.json: " + e.getMessage());
        }
    }
}