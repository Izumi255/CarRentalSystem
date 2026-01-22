package com.burak.carrentalsystem;

import com.burak.carrentalsystem.service.CarRentalService;

public class Main {
    public static void main(String[] args) {
        System.out.println("🚗 Запуск системи прокату автомобілів...");

        CarRentalService service = new CarRentalService();

        service.generateTestData(10, 5);

        service.printAllUsers();
        service.printAllCars();
        service.printRentalsHistory();

        System.out.println("\n✅ Програма завершена успішно.");
    }
}