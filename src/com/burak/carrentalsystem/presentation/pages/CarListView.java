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
            List<Car> carList = new ArrayList<>(carService.getAllCars());

            // --- ШАПКА ---
            System.out.println(ConsoleColors.CYAN_BOLD +
                    ConsoleColors.BOX_TOP_LEFT
                    + "════════════════════════════════════════════════════════════"
                    + ConsoleColors.BOX_TOP_RIGHT);
            System.out.println(ConsoleColors.BOX_VERTICAL
                    + "                🚗 ПАРК АВТОМОБІЛІВ КОМПАНІЇ              "
                    + ConsoleColors.BOX_VERTICAL);
            System.out.println(ConsoleColors.BOX_BOTTOM_LEFT
                    + "════════════════════════════════════════════════════════════"
                    + ConsoleColors.BOX_BOTTOM_RIGHT + ConsoleColors.RESET);

            if (carList.isEmpty()) {
                System.out.println(ConsoleColors.YELLOW + "\n  😕 Список порожній. Машин ще немає.");
                pause();
                return;
            }

            // --- ШАПКА ТАБЛИЦІ (Додана колонка "ЧАС") ---
            System.out.println();
            System.out.printf(ConsoleColors.BLUE_BOLD + "  %-4s  %-20s  %-10s  %-12s  %s%n"
                            + ConsoleColors.RESET,
                    "#", "🚘 АВТОМОБІЛЬ", "💰 ЦІНА", "📊 СТАТУС", "⏳ ЧАС");
            System.out.println(ConsoleColors.BLUE
                    + "  ────  ────────────────────  ──────────  ────────────  ────────"
                    + ConsoleColors.RESET);

            for (int i = 0; i < carList.size(); i++) {
                Car car = carList.get(i);
                String fullName = car.getBrand() + " " + car.getModel();
                if (fullName.length() > 18) {
                    fullName = fullName.substring(0, 17) + "…";
                }

                String status;
                String timeRemaining = "---";

                if (car.isAvailable()) {
                    status = ConsoleColors.GREEN_BOLD + "ВІЛЬНО   " + ConsoleColors.RESET;
                } else {
                    status = ConsoleColors.RED_BOLD + "ЗАЙНЯТО  " + ConsoleColors.RESET;

                    // Розрахунок залишку часу в хвилинах
                    long diff = car.getRentEndTime() - System.currentTimeMillis();
                    if (diff > 0) {
                        long mins = (diff / 60000) + 1; // Округлюємо в більшу сторону
                        timeRemaining = mins + " хв.";
                    }
                }

                String price = String.format("%.2f €", car.getPricePerHour());

                System.out.printf("  [%d]   %-20s  %-10s  %-12s  %s%n",
                        (i + 1), fullName, price, status, timeRemaining);
            }

            System.out.println(ConsoleColors.BLUE
                    + "  ──────────────────────────────────────────────────────────────"
                    + ConsoleColors.RESET);

            // --- МЕНЮ ---
            System.out.println(ConsoleColors.CYAN + "\n Введіть номер авто для оренди.");
            System.out.println(ConsoleColors.RED + " [0] Повернутися назад");

            System.out.println();
            ConsoleColors.printInline(ConsoleColors.YELLOW_BOLD,
                    " " + ConsoleColors.ARROW + " Ваш вибір > ");

            String input = scanner.nextLine().trim();
            if (input.equals("0")) {
                return;
            }

            try {
                int index = Integer.parseInt(input) - 1;
                if (index >= 0 && index < carList.size()) {
                    processRent(carList.get(index));
                } else {
                    ConsoleColors.print(ConsoleColors.RED_BOLD,
                            "\n " + ConsoleColors.ERROR_ICON + " Невірний номер.");
                    pause();
                }
            } catch (NumberFormatException e) {
                if (!input.isEmpty()) {
                    ConsoleColors.print(ConsoleColors.RED_BOLD,
                            "\n " + ConsoleColors.ERROR_ICON + " Введіть число!");
                    pause();
                }
            }
        }
    }

    private void processRent(Car car) {
        if (!car.isAvailable()) {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n " + ConsoleColors.ERROR_ICON + " Це авто вже в оренді.");
            pause();
            return;
        }

        System.out.println(ConsoleColors.CYAN + "\n ⏱ Оформлення оренди:");
        System.out.print(ConsoleColors.GREEN_BOLD + "  🕒 Тривалість (хв): " + ConsoleColors.RESET
                + ConsoleColors.ARROW + " ");

        int minutes = 60;
        try {
            String val = scanner.nextLine().trim();
            if (!val.isEmpty()) {
                minutes = Integer.parseInt(val);
            }
        } catch (NumberFormatException e) {
            System.out.println(ConsoleColors.YELLOW + "  ⚠️ Некоректно. Виставлено 60 хв.");
        }

        System.out.println(ConsoleColors.YELLOW + " ⏳ Транзакція...");
        try {
            Thread.sleep(700);
        } catch (Exception e) {
        }

        if (carService.rentCar(car.getId(), minutes)) {
            ConsoleColors.print(ConsoleColors.GREEN_BOLD,
                    "\n " + ConsoleColors.CHECK_ICON + " Успішно! Оренда на " + minutes
                            + " хв. активована.");
        } else {
            ConsoleColors.print(ConsoleColors.RED_BOLD,
                    "\n " + ConsoleColors.ERROR_ICON + " Помилка оренди.");
        }
        pause();
    }

    private void pause() {
        System.out.println(ConsoleColors.RESET + "\nНатисніть Enter...");
        scanner.nextLine();
    }
}