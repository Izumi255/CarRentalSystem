package com.burak.carrentalsystem.service;

import com.burak.carrentalsystem.model.Car;
import com.burak.carrentalsystem.repository.FileCarRepository;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList; // ✅ Додано
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarService {

    private final FileCarRepository carRepository;

    public CarService() {
        this.carRepository = new FileCarRepository();
    }

    // --- БІЗНЕС-ЛОГІКА ---

    // ✅ НОВИЙ МЕТОД (Саме його шукає AddCarForm)
    public void addCar(Car car) {
        // Тут ми просто зберігаємо вже створену машину
        carRepository.add(car);
        System.out.println("✅ Авто успішно додано: " + car.getBrand() + " " + car.getModel());
    }

    // Старий метод (може залишитися для сумісності)
    public void addCar(String brand, String model, double pricePerHour) {
        if (pricePerHour <= 0) {
            throw new IllegalArgumentException("❌ Помилка: Ціна оренди має бути більше 0!");
        }
        if (brand == null || brand.trim().isEmpty()) {
            throw new IllegalArgumentException("❌ Помилка: Бренд не може бути пустим!");
        }

        String id = "CAR-" + (System.currentTimeMillis() % 100000);
        Car newCar = new Car(id, brand, model,
                pricePerHour); // Тут використовується твій конструктор

        carRepository.add(newCar);
        System.out.println("✅ Авто успішно додано: " + brand + " " + model);
    }

    // Отримати тільки вільні авто
    public List<Car> getAvailableCars() {
        // ⚠️ ВИПРАВЛЕННЯ: Repository повертає Collection, тому перетворюємо в List
        Collection<Car> allCars = carRepository.getAll();

        List<Car> available = allCars.stream()
                .filter(Car::isAvailable)
                .collect(Collectors.toList());

        if (available.isEmpty()) {
            System.out.println("ℹ️ На жаль, вільних машин зараз немає.");
        }
        return available;
    }

    // Отримати всі авто
    public List<Car> getAllCars() {
        // ⚠️ ВИПРАВЛЕННЯ: Безпечне перетворення Collection -> List
        return new ArrayList<>(carRepository.getAll());
    }

    // Метод для експорту (ти його круто написав!)
    public void exportCarsToExcel(String filename) {
        System.out.println("📄 Починаю експорт даних у файл " + filename + "...");

        Collection<Car> cars = carRepository.getAll();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("ID,Brand,Model,Price Per Hour,Is Available");
            writer.newLine();

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

    public boolean rentCar(String carId) {
        Car car = carRepository.getById(carId).orElse(null);

        if (car == null) {
            System.out.println("❌ Авто з таким ID не знайдено.");
            return false;
        }

        if (!car.isAvailable()) {
            System.out.println("❌ Це авто вже зайняте!");
            return false;
        }

        // Бронюємо авто
        car.setAvailable(false);
        carRepository.add(car);

        System.out.println("✅ Ви успішно орендували " + car.getBrand() + " " + car.getModel());
        return true;
    }
}
