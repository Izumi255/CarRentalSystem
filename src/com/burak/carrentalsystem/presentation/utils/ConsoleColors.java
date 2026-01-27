package com.burak.carrentalsystem.presentation.utils;

public class ConsoleColors {

    public static final String RESET = "\033[0m";

    // --- Звичайні кольори ---
    public static final String RED = "\033[0;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String YELLOW = "\033[0;33m";
    public static final String BLUE = "\033[0;34m";
    public static final String PURPLE = "\033[0;35m";
    public static final String CYAN = "\033[0;36m";
    public static final String WHITE = "\033[0;37m";

    // --- Жирні кольори (для заголовків) ---
    public static final String RED_BOLD = "\033[1;31m";
    public static final String GREEN_BOLD = "\033[1;32m";
    public static final String YELLOW_BOLD = "\033[1;33m";
    public static final String BLUE_BOLD = "\033[1;34m";
    public static final String PURPLE_BOLD = "\033[1;35m";
    public static final String CYAN_BOLD = "\033[1;36m";
    public static final String WHITE_BOLD = "\033[1;37m";

    // --- Іконки та символи (Design Pack) ---
    public static final String CAR_ICON = "🚗";
    public static final String USER_ICON = "👤";
    public static final String KEY_ICON = "🔑";
    public static final String CHECK_ICON = "✅";
    public static final String ERROR_ICON = "❌";
    public static final String ARROW = "➜";

    // --- Рамки (для меню) ---
    public static final String BOX_TOP_LEFT = "╔";
    public static final String BOX_TOP_RIGHT = "╗";
    public static final String BOX_BOTTOM_LEFT = "╚";
    public static final String BOX_BOTTOM_RIGHT = "╝";
    public static final String BOX_HORIZONTAL = "═";
    public static final String BOX_VERTICAL = "║";

    public static void print(String color, String text) {
        System.out.println(color + text + RESET);
    }

    public static void printInline(String color, String text) {
        System.out.print(color + text + RESET);
    }

    // Реалізація очищення консолі (працює в більшості терміналів)
    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}