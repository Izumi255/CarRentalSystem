package com.burak.carrentalsystem.presentation.forms;

import com.burak.carrentalsystem.model.Car;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.service.CarService;

import java.util.Random;
import java.util.Scanner;
import java.util.UUID;

public class AddCarForm {

    private final CarService carService;
    private final Scanner scanner;
    private final Random random;

    // Масиви для генерації фейкових даних (DataFaker своїми руками)
    private final String[] fakeBrands = {"BMW", "Mercedes", "Audi", "Toyota", "Tesla", "Ford",
            "Honda", "Volkswagen"};
    private final String[] fakeModels = {"X5", "C-Class", "A6", "Camry", "Model S", "Mustang",
            "Civic", "Golf"};

    public AddCarForm() {
        this.carService = new CarService();
        this.scanner = new Scanner(System.in);
        this.random = new Random();
    }

    public void show() {
        ConsoleColors.print(ConsoleColors.CYAN, "\n➕ --- ДОДАВАННЯ НОВОГО АВТО ---");
        System.out.println(
                "(💡 Порада: Натисніть Enter без вводу, щоб згенерувати дані автоматично)");

        // 1. МАРКА (BRAND)
        System.out.print("Марка (Brand): ");
        String brand = scanner.nextLine().trim();

        if (brand.isEmpty()) {
            brand = fakeBrands[random.nextInt(fakeBrands.length)]; // Випадкова марка
            ConsoleColors.print(ConsoleColors.PURPLE, "🎲 Авто-генерація: " + brand);
        }

        // 2. МОДЕЛЬ (MODEL)
        System.out.print("Модель (Model): ");
        String model = scanner.nextLine().trim();

        if (model.isEmpty()) {
            model = fakeModels[random.nextInt(fakeModels.length)]; // Випадкова модель
            ConsoleColors.print(ConsoleColors.PURPLE, "🎲 Авто-генерація: " + model);
        }

        // 3. ЦІНА (PRICE)
        System.out.print("Ціна за годину (€): ");
        String priceInput = scanner.nextLine().trim();
        double price;

        if (priceInput.isEmpty()) {
            // Генеруємо ціну від 10.0 до 100.0
            price = 10 + (90 * random.nextDouble());
            // Округляємо до 2 знаків
            price = Math.round(price * 100.0) / 100.0;
            ConsoleColors.print(ConsoleColors.PURPLE, "🎲 Авто-генерація: " + price + " €");
        } else {
            try {
                price = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                ConsoleColors.print(ConsoleColors.RED, "❌ Ціна має бути числом! Скасовано.");
                return;
            }
        }

        // Створення авто
        Car newCar = new Car(UUID.randomUUID().toString(), brand, model, price);

        try {
            carService.addCar(newCar);
            // Тут повідомлення про успіх вже є в сервісі, але можна продублювати для краси
        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED, "❌ Помилка: " + e.getMessage());
        }
    }
}