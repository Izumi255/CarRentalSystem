package com.burak.CarRentalSystem;

public class Rental {
    private Car car;
    private Customer customer;
    private int hours;
    private double totalPrice;

    public Rental(Car car, Customer customer, int hours) {
        this.car = car;
        this.customer = customer;
        this.hours = hours;
        // Автоматично рахуємо ціну: ціна машини * години
        this.totalPrice = car.getPricePerHour() * hours;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    @Override
    public String toString() {
        return "============== ЧЕК ОРЕНДИ ==============\n" +
                "Автомобіль: " + car.getBrand() + " " + car.getModel() + "\n" +
                "Клієнт:     " + customer.getName() + "\n" +
                "Термін:     " + hours + " год.\n" +
                "ДО СПЛАТИ:  " + totalPrice + " €\n" +
                "========================================";
    }
}