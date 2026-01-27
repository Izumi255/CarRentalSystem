package com.burak.carrentalsystem.model;

public class Car {

    private String id;
    private String brand;
    private String model;
    private double pricePerHour;
    private boolean isAvailable;

    private long rentEndTime;

    public Car(String id, String brand, String model, double pricePerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;

        if (pricePerHour < 0) {
            System.out.println("⚠️ Увага: Ціна не може бути мінусовою! Встановлено 0.0");
            this.pricePerHour = 0.0;
        } else {
            this.pricePerHour = pricePerHour;
        }

        this.isAvailable = true;
        this.rentEndTime = 0; // Початково 0
    }

    // --- ГЕТТЕРИ ТА СЕТТЕРИ ---
    public String getId() {
        return id;
    }

    public String getBrand() {
        return brand;
    }

    public String getModel() {
        return model;
    }

    public double getPricePerHour() {
        return pricePerHour;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    // 🔥 Геттер і Сеттер для часу
    public long getRentEndTime() {
        return rentEndTime;
    }

    public void setRentEndTime(long rentEndTime) {
        this.rentEndTime = rentEndTime;
    }

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
        return String.format("[%s] %s %s | %.2f €/год | %s", id, brand, model, pricePerHour,
                status);
    }
}