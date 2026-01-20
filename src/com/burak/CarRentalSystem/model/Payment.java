package com.burak.CarRentalSystem.model;

import java.time.LocalDate;

public class Payment {
    private String paymentId;
    private double amount;
    private String paymentDate;
    private String paymentType;

    public Payment(double amount, String paymentType) {
        this.paymentId = "PAY-" + System.currentTimeMillis();

        if (amount < 0) {
            System.out.println("⚠️ Спроба оплати від'ємної суми!");
            this.amount = 0;
        } else {
            this.amount = amount;
        }
        this.paymentDate = LocalDate.now().toString();
        this.paymentType = paymentType;
    }
    @Override
    public String toString() {
        return "💰 Чек [" + paymentId + "] Сума: " + amount + "€ (" + paymentType + ")";
    }
}