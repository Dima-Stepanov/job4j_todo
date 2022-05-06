package ru.job4j.todo.store;

import java.util.Collection;
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
public interface Store<T> {

    boolean create(T type);

    Optional<T> findById(int id);

    boolean update(int id, T type);

    boolean delete(int id);

    List<T> findAll();

    List<T> findNew();

    List<T> findCompleted();

}
