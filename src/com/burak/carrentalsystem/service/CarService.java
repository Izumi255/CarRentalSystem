package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.model.Car;
import com.burak.carrentalsystem.repository.FileCarRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CarService {

    private final FileCarRepository carRepository;

    public CarService() {
        this.carRepository = new FileCarRepository();
    }

    public void addCar(Car car) {
        carRepository.add(car);
        System.out.println("✅ Авто успішно додано: " + car.getBrand() + " " + car.getModel());
    }

    // Отримати всі авто (з АВТОМАТИЧНОЮ перевіркою часу)
    public List<Car> getAllCars() {
        checkRentalsExpiration(); // 🔥 Перевіряємо, чи не час звільняти авто
        return new ArrayList<>(carRepository.getAll());
    }

    // 🔥 МАГІЯ: Перевіряє таймери
    private void checkRentalsExpiration() {
        long currentTime = System.currentTimeMillis();
        Collection<Car> allCars = carRepository.getAll();

        boolean needUpdate = false;

        for (Car car : allCars) {
            // Якщо авто зайняте І встановлено час закінчення І цей час уже минув
            if (!car.isAvailable() && car.getRentEndTime() > 0
                    && currentTime > car.getRentEndTime()) {

                car.setAvailable(true); // Звільняємо
                car.setRentEndTime(0);  // Скидаємо таймер
                carRepository.add(car); // Зберігаємо зміни в репозиторій
                needUpdate = true;
            }
        }

        if (needUpdate) {

        }
    }

    public boolean rentCar(String carId, int minutes) {
        Car car = carRepository.getById(carId).orElse(null);

        if (car == null) {
            System.out.println("❌ Авто з таким ID не знайдено.");
            return false;
        }

        if (!car.isAvailable()) {
            System.out.println("❌ Це авто вже зайняте!");
            return false;
        }

        car.setAvailable(false);

        long endTime = System.currentTimeMillis() + (minutes * 60_000L);
        car.setRentEndTime(endTime);

        carRepository.add(car); // Зберігаємо оновлений статус у файл/пам'ять

        System.out.println("✅ Ви успішно орендували " + car.getBrand() + " " + car.getModel());
        return true;
    }

    public void exportCarsToExcel(String filename) {
        System.out.println("📄 Починаю експорт даних у файл " + filename + "...");
        Collection<Car> cars = carRepository.getAll();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ID,Brand,Model,Price Per Hour,Is Available");
            writer.newLine();
            for (Car car : cars) {
                String line = String.format("%s,%s,%s,%.2f,%s",
                        car.getId(), car.getBrand(), car.getModel(), car.getPricePerHour(),
                        car.isAvailable() ? "Yes" : "No");
                writer.write(line);
                writer.newLine();
            }
            System.out.println("✅ Експорт успішний! Файл створено: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Помилка запису файлу: " + e.getMessage());
        }
    }
}