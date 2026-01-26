package com.burak.carrentalsystem.presentation.pages;

import com.burak.carrentalsystem.model.Car;
import com.burak.carrentalsystem.presentation.utils.ConsoleColors;
import com.burak.carrentalsystem.service.CarService;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CarListView {

    private final CarService carService;
    private final Scanner scanner;

    public CarListView() {
        this.carService = new CarService();
        this.scanner = new Scanner(System.in);
    }

    public void show() {
        while (true) {
            ConsoleColors.clearScreen();

            // ⚠️ ВАЖЛИВО: Перетворюємо Collection в List, щоб мати індекси (0, 1, 2...)
            List<Car> carList = new ArrayList<>(carService.getAllCars());

            ConsoleColors.print(ConsoleColors.CYAN, "\n🚗 --- ДОСТУПНІ АВТОМОБІЛІ ---");

            if (carList.isEmpty()) {
                ConsoleColors.print(ConsoleColors.YELLOW,
                        "😕 Список порожній. Адмін ще не додав авто.");
                System.out.println("Натисніть Enter, щоб вийти...");
                scanner.nextLine();
                return;
            }

            System.out.println(
                    "-----------------------------------------------------------------------------");
            // Замість ID пишемо "№" (Номер)
            System.out.printf("%-5s %-15s %-15s %-15s %-10s%n", "№", "БРЕНД", "МОДЕЛЬ", "ЦІНА/ГОД",
                    "СТАТУС");
            System.out.println(
                    "-----------------------------------------------------------------------------");

            // Виводимо список з порядковими номерами (i + 1)
            for (int i = 0; i < carList.size(); i++) {
                Car car = carList.get(i);

                String statusColor = car.isAvailable() ? ConsoleColors.GREEN : ConsoleColors.RED;
                String statusText = car.isAvailable() ? "ВІЛЬНО" : "ЗАЙНЯТО";

                // Тут ми показуємо (i + 1), тобто 1, 2, 3... замість довгого ID
                System.out.printf("%-5d %-15s %-15s %-15s %s%s%s%n",
                        (i + 1),
                        car.getBrand(),
                        car.getModel(),
                        String.format("%.2f €", car.getPricePerHour()),
                        statusColor, statusText, ConsoleColors.RESET
                );
            }
            System.out.println(
                    "-----------------------------------------------------------------------------");

            System.out.println("\nВведіть НОМЕР авто для оренди (наприклад 1) або '0' для виходу:");
            System.out.print("Ваш вибір > ");

            String input = scanner.nextLine();

            if (input.equals("0")) {
                return;
            }

            try {
                int choice = Integer.parseInt(input); // Перетворюємо текст в число
                int index = choice - 1; // В програмуванні індекси з 0, а у людей з 1

                if (index >= 0 && index < carList.size()) {
                    // 🔥 МАГІЯ: Дістаємо справжню машину за простим номером
                    Car selectedCar = carList.get(index);

                    // Викликаємо оренду, передаючи справжній ID
                    boolean success = carService.rentCar(selectedCar.getId());

                    if (success) {
                        System.out.println("Натисніть Enter, щоб продовжити...");
                        scanner.nextLine();
                    } else {
                        Thread.sleep(2000); // Пауза, щоб прочитати помилку
                    }
                } else {
                    ConsoleColors.print(ConsoleColors.RED, "❌ Невірний номер! Такої машини немає.");
                    Thread.sleep(1500);
                }

            } catch (NumberFormatException e) {
                ConsoleColors.print(ConsoleColors.RED, "❌ Введіть число!");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException ex) {
                }
            } catch (InterruptedException e) {
                // ігноруємо
            }
        }
    }
}