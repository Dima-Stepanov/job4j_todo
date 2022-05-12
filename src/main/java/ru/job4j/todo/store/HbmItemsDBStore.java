package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * 3. Лямбды и шаблон wrapper. [#49295]
 * HdmItemsDBStore хранилище в базе данных модели данных Item,
 * используется hibernate.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@Repository
public class HbmItemsDBStore implements Store<Item, User> {
    private final SessionFactory sf;

    public HbmItemsDBStore(final SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Сохранение заявки.
     * Применение шаблона WRAPPER.
     *
     * @param item Item
     * @return Boolean
     */
    @Override
    public boolean create(final Item item) {
        try {
            this.tx(session -> {
                        session.persist(item);
                        return true;
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Поиск заявки по id
     * Применение шаблона WRAPPER.
     *
     * @param id int
     * @return Optional<Item>
     */
    @Override
    public Optional<Item> findById(int id) {
        return this.tx(
                session -> Optional.ofNullable(session.get(Item.class, id))
        );
    }

    /**
     * Обновление заявки.
     * Применение шаблона WRAPPER.
     *
     * @param id   int
     * @param item Item
     * @return boolean
     */
    @Override
    public boolean update(int id, final Item item) {
        try {
            this.tx(session -> {
                        item.setId(id);
                        session.update(item);
                        return true;
                    }
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * Удаление заявки по id
     * Применение шаблона WRAPPER.
     *
     * @param id int
     * @return boolean
     */
    @Override
    public boolean delete(int id) {
        return this.tx(session -> {
                    Item item = session.get(Item.class, id);
                    session.delete(item);
                    return true;
                }
        );
    }

    /**
     * Поиск всех заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List
     */
    @Override
    public List<Item> findAll(final User user) {
        return this.tx(
                session -> session
                        .createQuery("from Item where user=:user")
                        .setParameter("user", user).list()
        );
    }

    /**
     * Поиск незавершенных заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List.
     */
    @Override
    public List<Item> findNew(final User user) {
        return this.tx(
                session -> session
                        .createQuery("from Item where user=:user and done is null")
                        .setParameter("user", user).list()
        );
    }

    /**
     * Поиск завершенных заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List.
     */
    @Override
    public List<Item> findDone(final User user) {
        return this.tx(
                session -> session
                        .createQuery("from Item where user=:user and done is not null")
                        .setParameter("user", user).list()
        );
    }

    /**
     * Шаблон проектирования WRAPPER.
     *
     * @param command Function
     * @param <T>     T
     * @return T
     */
    private <T> T tx(final Function<Session, T> command) {
        final Session session = sf.openSession();
        final Transaction tx = session.beginTransaction();
        try {
            T rsl = command.apply(session);
            tx.commit();
            return rsl;
        } catch (final Exception e) {
            session.getTransaction().rollback();
            throw e;
        } finally {
            session.close();
        }
    }
}
