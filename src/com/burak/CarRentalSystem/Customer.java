package com.burak.CarRentalSystem;

public class Customer {

    private String id;
    private String name;
    private String phone;
    private String passport;
    private double balance;

    // Конструктор
    public Customer(String id, String name, String phone, String passport, double balance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.passport = passport;
        this.balance = balance;
    }


    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    // МЕТОД 1: Поповнення рахунку
    public void addBalance(double amount) {
        if (amount > 0) {
            this.balance += amount;
            System.out.println("-> Рахунок " + name + " поповнено на " + amount + "€. Баланс: " + this.balance + "€");
        }
    }


    public boolean charge(double amount) {
        if (this.balance >= amount) {
            this.balance -= amount;
            return true; // Операція успішна
        } else {
            System.out.println("-> Помилка! Недостатньо коштів у " + name + ". Треба: " + amount + "€, Є: " + this.balance + "€");
            return false; // Операція провалена
        }
    }

    @Override
    public String toString() {
        return "Клієнт: " + name + " [Паспорт: " + passport + "] - Баланс: " + balance + "€";
    }
}