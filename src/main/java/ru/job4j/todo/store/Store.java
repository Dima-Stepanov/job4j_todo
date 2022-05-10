package ru.job4j.todo.store;

import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * Store interface описывает CRUD поведение для хранилищ.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
public interface Store<T, U> {

    void create(final T type);

    Optional<T> findById(int id);

    boolean update(int id, final T type);

    boolean delete(int id);

    List<T> findAll(final U user);

    List<T> findNew(final U user);

    List<T> findDone(final U user);
}
