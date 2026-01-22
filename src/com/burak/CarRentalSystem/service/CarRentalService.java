package com.burak.CarRentalSystem.service;

import com.burak.CarRentalSystem.model.*;
import com.burak.CarRentalSystem.repository.*; // <--- Підключили наші нові репозиторії
import net.datafaker.Faker;

import java.util.List;

public class CarRentalService {
    // Тепер ми використовуємо не List, а Репозиторії (наші "склади")
    private final CrudRepository<Car> carRepository;
    private final CrudRepository<User> userRepository;
    private final FileRentalRepository rentalRepository; // Тут беремо конкретний клас, щоб мати доступ до пошуку по юзеру

    private final Faker faker;

    public CarRentalService() {
        // Ініціалізуємо репозиторії. Вони самі підтягнуть дані з файлів!
        this.carRepository = new FileCarRepository();
        this.userRepository = new FileUserRepository();
        this.rentalRepository = new FileRentalRepository();
        this.faker = new Faker();
    }

    // --- ГЕНЕРАЦІЯ ТЕСТОВИХ ДАНИХ (Оновлена) ---
    public void generateTestData(int carsCount, int usersCount) {
        System.out.println("🔄 Починаю генерацію даних...");

        // 1. Генеруємо Машини (якщо їх ще немає)
        if (carRepository.getAll().isEmpty()) {
            for (int i = 0; i < carsCount; i++) {
                String id = "CAR-" + faker.number().digits(4);
                Car newCar = new Car(id, faker.vehicle().manufacturer(), faker.vehicle().model(), faker.number().randomDouble(2, 10, 200));
                carRepository.add(newCar); // <--- Зберігаємо через репозиторій
            }
            System.out.println("🚗 Машини згенеровано і збережено в cars.json");
        } else {
            System.out.println("ℹ️ Машини вже є в базі, пропускаємо генерацію.");
        }

        // 2. Створюємо АДМІНА (якщо немає)
        if (userRepository.getById("admin").isEmpty()) {
            User admin = new User(
                    "admin",
                    "System Administrator",
                    "admin",
                    "admin@rental.com",
                    "+380-00-000-0000",
                    "Admin Office",
                    Role.ADMIN
            );
            userRepository.add(admin);
            System.out.println("🛡️ Створено Адміністратора (admin/admin)");
        }

        // 3. Генеруємо КЛІЄНТІВ (якщо мало)
        if (userRepository.getAll().size() < usersCount) {
            for (int i = 0; i < usersCount; i++) {
                String username = faker.name().firstName().toLowerCase() + faker.number().digits(3);
                String fullName = faker.name().fullName();
                String password = "123";
                String email = faker.internet().emailAddress();
                String phone = faker.numerify("+380-##-###-####");
                String address = faker.address().fullAddress();

                User user = new User(username, fullName, password, email, phone, address, Role.CUSTOMER);
                userRepository.add(user);
            }
            System.out.println("👥 Користувачів додано в users.json");
        }

        // 4. Генеруємо Оренди (для тесту)
        if (rentalRepository.getAll().isEmpty()) {
            List<User> allUsers = userRepository.getAll();
            List<Car> allCars = carRepository.getAll();

            for (int i = 0; i < 5; i++) {
                User randomUser = allUsers.get(faker.number().numberBetween(0, allUsers.size()));
                if (randomUser.isAdmin()) continue;

                Car randomCar = allCars.get(faker.number().numberBetween(0, allCars.size()));
                Rental rental = new Rental(randomCar, randomUser, faker.number().numberBetween(1, 14));

                rentalRepository.add(rental);
            }
            System.out.println("📝 Оренди створено в rentals.json");
        }

        System.out.println("✅ Дані готові.");
    }

    // --- МЕТОДИ ДЛЯ РОБОТИ (Тепер просто викликають репозиторії) ---

    public void printAllCars() {
        System.out.println("\n--- 🚗 СПИСОК АВТОМОБІЛІВ ---");
        List<Car> cars = carRepository.getAll();
        for (Car car : cars) {
            System.out.println(car);
        }
    }

    public void printAllUsers() {
        System.out.println("\n--- 👥 СПИСОК КОРИСТУВАЧІВ ---");
        List<User> users = userRepository.getAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    public void printRentalsHistory() {
        System.out.println("\n--- 📝 ІСТОРІЯ ОРЕНД ---");
        List<Rental> rentals = rentalRepository.getAll();
        for (Rental rental : rentals) {
            System.out.println(rental);
        }
    }
}