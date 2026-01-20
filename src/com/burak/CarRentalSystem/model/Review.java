package com.burak.CarRentalSystem.model;

public class Review {
    private String authorName;
    private int rating; // 1-5 зірок
    private String comment;

    public Review(String authorName, int rating, String comment) {
        this.authorName = authorName;

        // Валідація рейтингу (1-5)
        if (rating < 1) this.rating = 1;
        else if (rating > 5) this.rating = 5;
        else this.rating = rating;

        this.comment = comment;
    }

    @Override
    public String toString() {
        return "⭐ " + rating + "/5 від " + authorName + ": \"" + comment + "\"";
    }
}