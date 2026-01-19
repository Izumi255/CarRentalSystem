package com.burak.CarRentalSystem;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Підключаємо наш сервіс (базу даних)
        CarRentalService service = new CarRentalService();
        // Вмикаємо сканер (читання з клавіатури)
        Scanner scanner = new Scanner(System.in);

        Customer currentUser = null; // Тут буде зберігатися той, хто увійшов

        while (true) {
            System.out.println("\n=== МЕНЮ ===");

            if (currentUser == null) {
                System.out.println("1. Зареєструватися");
                System.out.println("0. Вихід");
            } else {
                System.out.println("Привіт, " + currentUser.getName() + " | Баланс: " + currentUser.getBalance() + "€");
                System.out.println("2. Показати машини");
                System.out.println("3. Взяти авто в оренду");
                System.out.println("4. Поповнити рахунок");
                System.out.println("0. Вихід");
            }

            System.out.print("Ваш вибір: ");
            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    if (currentUser != null) {
                        System.out.println("Ви вже увійшли!");
                        break;
                    }
                    System.out.print("Введіть ваше ім'я: ");
                    String name = scanner.nextLine();
                    System.out.print("Введіть телефон: ");
                    String phone = scanner.nextLine();
                    System.out.print("Введіть паспорт: ");
                    String passport = scanner.nextLine();

                    // Реєструємо через наш сервіс
                    currentUser = service.registerCustomer(name, phone, passport);
                    System.out.println("✅ Ви успішно зареєстровані! Вам нараховано 100€ бонусу.");
                    break;

                case "2":
                    if (currentUser == null) break;
                    service.showAvailableCars();
                    break;

                case "3":
                    if (currentUser == null) break;
                    service.showAvailableCars();
                    System.out.print("Введіть ID машини (наприклад, C001): ");
                    String carId = scanner.nextLine();
                    System.out.print("На скільки годин?: ");
                    // Треба зчитати число і очистити буфер
                    int hours = Integer.parseInt(scanner.nextLine());

                    service.rentCar(currentUser, carId, hours);
                    break;

                case "4":
                    if (currentUser == null) break;
                    System.out.print("Сума поповнення: ");
                    double amount = Double.parseDouble(scanner.nextLine());
                    currentUser.addBalance(amount);
                    break;

                case "0":
                    System.out.println("До побачення!");
                    return; // Завершує програму

                default:
                    System.out.println("Невідома команда.");
            }
        }
    }
}