package com.burak.CarRentalSystem.model;

public class Rental {
    private String id;
    private Car car;
    private User user;
    private Payment payment;
    private Review review;
    private int days;
    public User getUser() {
        return user;
    }

    public Rental(Car car, User user, int days) {
        this.id = "RENT-" + (int)(Math.random() * 10000);
        this.car = car;
        this.user = user;
        this.days = days;

        // Розрахунок ціни
        double totalCost = car.getPricePerHour() * 24 * days;
        this.payment = new Payment(totalCost, "Credit Card");
    }

    // Метод для відгуку
    public void leaveReview(int rating, String comment) {
        // Беремо ім'я з юзера
        this.review = new Review(user.getFullName(), rating, comment);
    }

    @Override
    public String toString() {
        String reviewText = (review != null) ? "\n   " + review.toString() : "";
        return String.format("📝 Оренда %s\n   %s\n   %s\n   %s%s",
                id, car.toString(), user.toString(), payment.toString(), reviewText);
    }
}