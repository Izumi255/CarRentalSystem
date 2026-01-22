package com.burak.carrentalsystem.repository;

import java.util.List;
import java.util.Optional;

// <T> — це дженерік (туди підставимо User або Car)
// <ID> — тип ідентифікатора (у нас String)
public interface CrudRepository<T> {

    // Створити (Create)
    void add(T entity);
    Optional<T> getById(String id);

    // Отримати всіх (Read All)
    List<T> getAll();
    // Оновити (Update)
    void update(T entity);
    // Видалити (Delete)
    boolean delete(String id);
}