package com.burak.CarRentalSystem;

import java.util.ArrayList;
import java.util.List;

public class CarRentalService {
    // Це наша "База даних" у пам'яті
    private List<Car> cars = new ArrayList<>();
    private List<Customer> customers = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    public CarRentalService() {
        // Додамо декілька машин відразу, щоб було з чого вибирати
        cars.add(new Car("C001", "Toyota", "Camry", 10.0));
        cars.add(new Car("C002", "BMW", "X5", 20.0));
        cars.add(new Car("C003", "Tesla", "Model 3", 25.0));
    }

    // Метод реєстрації нового клієнта
    public Customer registerCustomer(String name, String phone, String passport) {
        // Генеруємо ID автоматично (просто беремо розмір списку + 1)
        String id = "USER-" + (customers.size() + 1);
        // Даруємо новому клієнту бонус 100 євро для старту
        Customer newCustomer = new Customer(id, name, phone, passport, 100.0);
        customers.add(newCustomer);
        return newCustomer;
    }

    // Показати всі вільні машини
    public void showAvailableCars() {
        System.out.println("\n--- Доступні автомобілі ---");
        for (Car car : cars) {
            if (car.isAvailable()) {
                System.out.println(car);
            }
        }
    }

    // Знайти машину за ID (коли клієнт вводить "C001")
    public Car findCarById(String carId) {
        for (Car car : cars) {
            if (car.getId().equalsIgnoreCase(carId)) {
                return car;
            }
        }
        return null;
    }

    // Оформити оренду
    public void rentCar(Customer customer, String carId, int hours) {
        Car car = findCarById(carId);

        if (car == null) {
            System.out.println("❌ Машину з таким ID не знайдено.");
            return;
        }
        if (!car.isAvailable()) {
            System.out.println("❌ Ця машина вже зайнята.");
            return;
        }

        // Створюємо замовлення
        Rental rental = new Rental(car, customer, hours);

        // Пробуємо списати гроші
        if (customer.charge(rental.getTotalPrice())) {
            car.setAvailable(false); // Машина тепер зайнята
            rentals.add(rental);     // Зберігаємо в історію
            System.out.println("✅ Успішно! Ось ваш чек:");
            System.out.println(rental);
        }
    }
}