package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * 3. Лямбды и шаблон wrapper. [#49295]
 * HdmItemDBStore хранилище в базе данных модели данных Item,
 * используется hibernate.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@Repository
public class HbmItemsDBStore implements Store<Item> {
    private final SessionFactory sf;

    public HbmItemsDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Сохранение заявки.
     * Применение шаблона WRAPPER.
     *
     * @param item Item
     * @return Optional<Item>
     */
    @Override
    public Optional<Item> create(Item item) {
        return this.tx(
                session -> {
                    session.save(item);
                    return Optional.ofNullable(session.get(Item.class, item.getId()));
                }
        );
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
    public boolean update(int id, Item item) {
        return this.tx(
                session -> session.createQuery("update Item set name=:name, description=:description, "
                                + "created=:created, done=:done where id=:id")
                        .setParameter("id", id)
                        .setParameter("name", item.getName())
                        .setParameter("description", item.getDescription())
                        .setParameter("created", item.getCreated())
                        .setParameter("done", item.getDone())
                        .executeUpdate() > 0
        );
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
        return this.tx(
                session -> session.createQuery(
                                "delete Item where id=:id")
                        .setParameter("id", id).executeUpdate() > 0
        );
    }

    /**
     * Поиск всех заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List
     */
    @Override
    public List<Item> findAll() {
        return this.tx(
                session -> session.createQuery(
                        "from Item").list()
        );
    }

    /**
     * Поиск незавершенных заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List.
     */
    @Override
    public List<Item> findNew() {
        return this.tx(
                session -> session.createQuery(
                        "from Item where done is null ").list()
        );
    }

    /**
     * Поиск завершенных заявок.
     * Применение шаблона WRAPPER.
     *
     * @return List.
     */
    @Override
    public List<Item> findCompleted() {
        return this.tx(
                session -> session.createQuery(
                        "from Item where done is not null").list()
        );
    }

    /**
     * Шаблон проектирования WRAPPER.
     *
     * @param command Function
     * @param <T>
     * @return
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
