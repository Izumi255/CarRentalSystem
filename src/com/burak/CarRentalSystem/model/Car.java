package com.burak.CarRentalSystem.model;

public class Car {
    private String id;
    private String brand;
    private String model;
    private double pricePerHour;
    private boolean isAvailable; // Чи вільна машина?


    public Car(String id, String brand, String model, double pricePerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;

        // ВАЛІДАЦІЯ: Ціна не може бути менше 0
        if (pricePerHour < 0) {
            System.out.println("⚠️ Увага: Ціна не може бути мінусовою! Встановлено 0.0");
            this.pricePerHour = 0.0;
        } else {
            this.pricePerHour = pricePerHour;
        }

        this.isAvailable = true; // Нова машина завжди вільна
    }

    // --- ГЕТТЕРИ
    public String getId() { return id; }
    public String getBrand() { return brand; }
    public String getModel() { return model; }
    public double getPricePerHour() { return pricePerHour; }
    public boolean isAvailable() { return isAvailable; }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setPricePerHour(double pricePerHour) {
        if (pricePerHour >= 0) {
            this.pricePerHour = pricePerHour;
        }
    }

    @Override
    public String toString() {
        String status = isAvailable ? "✅ Вільна" : "❌ Зайнята";
        return String.format("[%s] %s %s | %.2f €/год | %s", id, brand, model, pricePerHour, status);
    }
}