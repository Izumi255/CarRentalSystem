package com.burak.CarRentalSystem;

import com.burak.CarRentalSystem.service.CarRentalService;

public class Main {
    public static void main(String[] args) {
        CarRentalService service = new CarRentalService();

        service.generateTestData(1, 5);

        service.printAllData();

        service.saveToJson("rental_data.json");
    }
}