package com.burak.carrentalsystem.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T> {

    // Створити
    void add(T entity);

    Optional<T> getById(String id);

    // Отримати всіх
    List<T> getAll();

    // Оновити
    void update(T entity);

    // Видалити
    boolean delete(String id);
}