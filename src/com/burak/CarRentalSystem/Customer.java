package com.burak.CarRentalSystem;

public class Customer {
    // Поля (характеристики) з нашої діаграми
    private String id;
    private String name;      // ПІБ
    private String phone;     // Телефон
    private String passport;  // Паспорт
    private double balance;   // Гроші (Євро)

    // Конструктор
    public Customer(String id, String name, String phone, String passport, double balance) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.passport = passport;
        this.balance = balance;
    }

    // Геттери (щоб дізнатися ім'я та баланс)
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

    // МЕТОД 2: Списання грошей (повертає true, якщо успішно, і false, якщо мало грошей)
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