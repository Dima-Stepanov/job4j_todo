package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.store.Store;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * ItemService слой бизнес логики.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 01.05.2022
 */
@Service
public class ItemsService {
    private final Store<Item, User> store;

    public ItemsService(Store<Item, User> store) {
        this.store = store;
    }

    /**
     * Создание заявки.
     *
     * @param item Item.
     */
    public void add(Item item) {
        store.create(item);
    }

    /**
     * Поиск заявки по id
     *
     * @param id int
     * @return Optional.
     */
    public Optional<Item> findByIdItem(int id) {
        return store.findById(id);
    }

    /**
     * Обеолвение заявки по id.
     *
     * @param id   int
     * @param item Item
     * @return boolean
     */
    public boolean updateItem(int id, Item item) {
        return store.update(id, item);
    }

    /**
     * Закрытие задачи, установка времени закрытия задачи.
     *
     * @param id int
     * @return String.
     */
    public boolean doneItem(int id) {
        boolean result = false;
        Optional<Item> item = store.findById(id);
        if (item.isPresent()) {
            item.get().setDone(new Date(System.currentTimeMillis()));
            store.update(item.get().getId(), item.get());
            result = true;
        }
        return result;
    }

    /**
     * Удаление заявки по id.
     *
     * @param id int.
     * @return boolean.
     */
    public boolean deleteItem(int id) {
        return store.delete(id);
    }

    /**
     * Поиск всех заявок в системе.
     *
     * @return List.
     */
    public List<Item> findAllItem(final User user) {
        return store.findAll(user);
    }

    /**
     * Поиск новых заявок.
     *
     * @return List
     */
    public List<Item> findNewItem(final User user) {
        return store.findNew(user);
    }

    /**
     * Поиск завершенных заявок.
     *
     * @return List.
     */
    public List<Item> findDoneItem(final User user) {
        return store.findDone(user);
    }
}
