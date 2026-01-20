package com.burak.CarRentalSystem.service;

import com.burak.CarRentalSystem.model.*;
import net.datafaker.Faker;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarRentalService {
    // Списки для зберігання наших даних
    private List<Car> cars = new ArrayList<>();
      private List<User> users = new ArrayList<>();
    private List<Rental> rentals = new ArrayList<>();

    // Генератор випадкових даних
    private Faker faker = new Faker(new Locale("en"));

    //  ДАНИХ
    public void generateTestData(int carsCount, int usersCount) {
        System.out.println("🔄 Починаю генерацію даних...");

        //Машини
        for (int i = 0; i < carsCount; i++) {
            String id = "CAR-" + faker.number().digits(4);
            Car newCar = new Car(id, faker.vehicle().manufacturer(), faker.vehicle().model(), faker.number().randomDouble(2, 10, 200));
            cars.add(newCar);
        }

        // 2. СтворюємоАдміністратора
        User admin = new User(
                "admin",
                "System Administrator",
                "admin",
                "admin@rental.com",
                "+380-00-000-0000",
                "Admin Office",
                Role.ADMIN // <--- Чітко вказуємо роль ADMIN
        );
        users.add(admin);
        System.out.println("🛡️ Створено Адміністратора: логін 'admin', пароль 'admin'");

        for (int i = 0; i < usersCount; i++) {
            String username = faker.name().firstName().toLowerCase() + faker.number().digits(3);
            String fullName = faker.name().fullName();
            String password = "123";
            String email = faker.internet().emailAddress();
            String phone = faker.numerify("+380-##-###-####");
            String address = faker.address().fullAddress();

            User user = new User(username, fullName, password, email, phone, address, Role.CUSTOMER);
            users.add(user);
        }

        for (int i = 0; i < usersCount / 2; i++) {
            User randomUser = users.get(faker.number().numberBetween(0, users.size()));

            if (randomUser.isAdmin()) continue;

            Car randomCar = cars.get(faker.number().numberBetween(0, cars.size()));
            Rental rental = new Rental(randomCar, randomUser, faker.number().numberBetween(1, 14));

            if (faker.bool().bool()) {
                rental.leaveReview(faker.number().numberBetween(1, 5), faker.yoda().quote());
            }
            rentals.add(rental);
        }

        System.out.println("✅ Дані згенеровано: 1 Адмін та " + usersCount + " Користувачів.");
    }

    // Друк даних
    public void printAllData() {
        System.out.println("\n--- 🚗 СПИСОК АВТОМОБІЛІВ ---");
        for (Car car : cars) System.out.println(car);

        System.out.println("\n--- 👥 СПИСОК КОРИСТУВАЧІВ (Admin/User) ---");
        for (User user : users) System.out.println(user);

        System.out.println("\n--- 📝 ІСТОРІЯ ОРЕНД ---");
        for (Rental rental : rentals) System.out.println(rental);
    }

    // Збереження у файл
    public void saveToJson(String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filename)) {
            gson.toJson(rentals, writer);
            System.out.println("💾 Успішно збережено у файл: " + filename);
        } catch (IOException e) {
            System.err.println("❌ Помилка при записі файлу: " + e.getMessage());
        }
    }
}