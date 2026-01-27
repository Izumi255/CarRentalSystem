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

    // Масиви для генерації фейкових даних
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
        ConsoleColors.clearScreen();

        // --- ШАПКА ---
        System.out.println(ConsoleColors.CYAN_BOLD +
                ConsoleColors.BOX_TOP_LEFT + "══════════════════════════════════════════════"
                + ConsoleColors.BOX_TOP_RIGHT);
        System.out.println(
                ConsoleColors.BOX_VERTICAL + "          ➕ ДОДАВАННЯ НОВОГО АВТО            "
                        + ConsoleColors.BOX_VERTICAL);
        System.out.println(
                ConsoleColors.BOX_BOTTOM_LEFT + "══════════════════════════════════════════════"
                        + ConsoleColors.BOX_BOTTOM_RIGHT +
                        ConsoleColors.RESET);

        System.out.println(ConsoleColors.YELLOW
                + " 💡 Порада: Натисніть [Enter] без вводу, щоб згенерувати дані автоматично 🎲\n"
                + ConsoleColors.RESET);

        // 1. МАРКА (BRAND)
        System.out.print(ConsoleColors.GREEN_BOLD + "  🚘  Марка (Brand): " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String brand = scanner.nextLine().trim();

        if (brand.isEmpty()) {
            brand = fakeBrands[random.nextInt(fakeBrands.length)];
            System.out.println(ConsoleColors.PURPLE_BOLD + "     🎲 Авто-вибір: " + brand
                    + ConsoleColors.RESET);
        }

        // 2. МОДЕЛЬ (MODEL)
        System.out.print(ConsoleColors.GREEN_BOLD + "  🏎️   Модель (Model): " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String model = scanner.nextLine().trim();

        if (model.isEmpty()) {
            model = fakeModels[random.nextInt(fakeModels.length)];
            System.out.println(ConsoleColors.PURPLE_BOLD + "     🎲 Авто-вибір: " + model
                    + ConsoleColors.RESET);
        }

        // 3. ЦІНА (PRICE)
        System.out.print(ConsoleColors.GREEN_BOLD + "  💰  Ціна (€/год):  " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");
        String priceInput = scanner.nextLine().trim();
        double price;

        if (priceInput.isEmpty()) {
            price = 10 + (90 * random.nextDouble());
            price = Math.round(price * 100.0) / 100.0;
            System.out.println(
                    ConsoleColors.PURPLE_BOLD + "     🎲 Згенерована ціна: " + price + " €"
                            + ConsoleColors.RESET);
        } else {
            try {
                price = Double.parseDouble(priceInput);
            } catch (NumberFormatException e) {
                ConsoleColors.print(ConsoleColors.RED_BOLD,
                        "\n  " + ConsoleColors.ERROR_ICON + " Помилка: Ціна має бути числом!");
                pressEnterToContinue();
                return;
            }
        }

        System.out.println(ConsoleColors.CYAN + "  ──────────────────────────────────────────"
                + ConsoleColors.RESET);

        // Створення авто
        Car newCar = new Car(UUID.randomUUID().toString(), brand, model, price);

        try {
            System.out.println(ConsoleColors.YELLOW + "  ⏳ Збереження в базу...");
            Thread.sleep(800);

            carService.addCar(newCar);

            // --- КАРТКА РЕЗУЛЬТАТУ ---
            System.out.println(ConsoleColors.GREEN_BOLD + "\n  " + ConsoleColors.CHECK_ICON
                    + "  АВТОМОБІЛЬ УСПІШНО ДОДАНО!");
            System.out.println(ConsoleColors.GREEN + "  ╔══════════════════════════════════╗");
            System.out.printf("  ║ %-32s ║\n", "🚘 " + brand + " " + model);
            System.out.printf("  ║ %-32s ║\n", "💰 " + price + " € / година");
            System.out.println("  ╚══════════════════════════════════╝" + ConsoleColors.RESET);

            pressEnterToContinue();

        } catch (Exception e) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n  " + ConsoleColors.ERROR_ICON + " Помилка при збереженні: "
                            + e.getMessage());
            pressEnterToContinue();
        }
    }

    private void pressEnterToContinue() {
        System.out.println(ConsoleColors.RESET + "\nНатисніть Enter, щоб продовжити...");
        scanner.nextLine();
    }
}