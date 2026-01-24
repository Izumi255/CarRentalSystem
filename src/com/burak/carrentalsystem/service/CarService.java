package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.model.Car;
import com.burak.carrentalsystem.repository.FileCarRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private final FileCarRepository carRepository;

    public CarService() {
        this.carRepository = new FileCarRepository();
    }

    //БІЗНЕС-ЛОГІКА

    public void addCar(String brand, String model, double pricePerHour) {
        if (pricePerHour <= 0) {
            throw new IllegalArgumentException("❌ Помилка: Ціна оренди має бути більше 0!");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Помилка: Бренд не може бути пустим!");
        }

        String id = "CAR-" + (System.currentTimeMillis() % 100000);
        Car newCar = new Car(id, brand, model, pricePerHour);

        carRepository.add(newCar);
        System.out.println("✅ Авто успішно додано: " + brand + " " + model);
    }

    public List<Car> getAvailableCars() {
        List<Car> allCars = carRepository.getAll();

        List<Car> available = allCars.stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.println("ℹ️ На жаль, вільних машин зараз немає.");
        }
        return available;
    }

    public List<Car> getAllCars() {
        return carRepository.getAll();
    }


    public void exportCarsToExcel(String filename) {
        System.out.println("📄 Починаю експорт даних у файл " + filename + "...");

        List<Car> cars = carRepository.getAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            // 1. Пишемо заголовки стовпців (як в Excel)
            writer.write("ID,Brand,Model,Price Per Hour,Is Available");
            writer.newLine(); // Перехід на новий рядок

            // 2. Пишемо дані про кожну машину
            for (Car car : cars) {
                String line = String.format("%s,%s,%s,%.2f,%s",
                        car.getId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getPricePerHour(),
                        car.isAvailable() ? "Yes" : "No"
                );
                writer.write(line);
                writer.newLine();
            }
            System.out.println("✅ Експорт успішний! Файл створено: " + filename);
            System.out.println("ℹ️ Ви можете відкрити його в Excel.");

        } catch (IOException e) {
            System.err.println("❌ Помилка запису файлу: " + e.getMessage());
        }
    }
}