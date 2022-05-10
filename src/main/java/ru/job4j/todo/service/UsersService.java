package ru.job4j.todo.service;


import org.springframework.stereotype.Service;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.HbmUsersDBStore;

import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * UserService слой бизнес логики.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 8.05.2022
 */
@Service
public class UsersService {
    private final HbmUsersDBStore userStore;

    public UsersService(HbmUsersDBStore userStore) {
        this.userStore = userStore;
    }

    /**
     * Создание пользователя.
     *
     * @param user User
     * @return boolean
     */
    public boolean create(final User user) {
        return userStore.create(user);
    }

    /**
     * Поиск пользователя по id.
     *
     * @param id int
     * @return Optional.
     */
    public Optional<User> findById(int id) {
        return userStore.findById(id);
    }

    public boolean updateUser(int id, final User user) {
        return userStore.update(id, user);
    }

    /**
     * Посик полльзователя по имени и паролю.
     *
     * @param name     String
     * @param password String
     * @return Optional
     */
    public Optional<User> findByNamePassword(final String name, final String password) {
        return userStore.findByNamePassword(name, password);
    }
}
