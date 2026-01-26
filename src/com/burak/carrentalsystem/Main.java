package com.burak.carrentalsystem;

import com.burak.carrentalsystem.presentation.pages.AuthView;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class Main {

    public static void main(String[] args) {
        // Виправлення кодування для Windows (щоб кирилиця відображалася коректно)
        System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8));

        // Запуск програми через Presentation Layer
        AuthView authView = new AuthView();
        authView.show();
    }
}