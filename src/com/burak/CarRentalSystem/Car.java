package com.burak.CarRentalSystem; // Ось твоя правильна назва

public class Car {
    // ... весь інший код залишається таким самим ...
    private String id;
    private String brand;
    private String model;
    private double pricePerHour;
    private boolean isAvailable;

    public Car(String id, String brand, String model, double pricePerHour) {
        this.id = id;
        this.brand = brand;
        this.model = model;
        this.pricePerHour = pricePerHour;
        this.isAvailable = true;
    }

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

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    @Override
    public String toString() {
        String status = isAvailable ? "Вільна" : "Зайнята";
        return String.format("[%s] %s %s - %.2f €/год (%s)",
                id, brand, model, pricePerHour, status);
    }
}