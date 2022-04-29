package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * HdmItemDBStore хранилище в базе данных модели данных Item,
 * используется hibernate.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@Repository
public class HbmItemDBStore implements Store<Item> {
    private final SessionFactory sf;

    public HbmItemDBStore(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Сохранение заявки.
     *
     * @param item Item
     * @return Optional<Item>
     */
    @Override
    public Optional<Item> create(Item item) {
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        Optional<Item> result = Optional.ofNullable(session.get(Item.class, item.getId()));
        Transaction tr = session.getTransaction();
        tr.commit();
        session.close();
        return result;
    }

    /**
     * Поиск заявки по id
     *
     * @param id int
     * @return Optional<Item>
     */
    @Override
    public Optional<Item> findById(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<Item> result = Optional.ofNullable(session.get(Item.class, id));
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Обновление заявки.
     *
     * @param id   int
     * @param item Item
     * @return boolean
     */
    @Override
    public boolean update(int id, Item item) {
        boolean result = false;
        Session session = sf.openSession();
        session.beginTransaction();
        item.setId(id);
        session.update(item);
        Optional<Item> updateItem = Optional.ofNullable(session.get(Item.class, item.getId()));
        session.getTransaction().commit();
        session.close();
        if (updateItem.isPresent()) {
            result = item.equals(updateItem.get());
        }
        return result;
    }

    /**
     * Удаление заявки по id
     *
     * @param id int
     * @return boolean
     */
    @Override
    public boolean delete(int id) {
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("delete from Item where id = :idItem")
                .setParameter("idItem", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        return result > 0;
    }

    /**
     * Поиск всех заявок.
     *
     * @return List
     */
    @Override
    public List<Item> findAll() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item")
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Поиск незавершенных заявок.
     *
     * @return List.
     */
    @Override
    public List<Item> findNew() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item  where done is null")
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }

    /**
     * Поиск завершенных заявок.
     *
     * @return List.
     */
    @Override
    public List<Item> findCompleted() {
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item  where done is not null")
                .list();
        session.getTransaction().commit();
        session.close();
        return result;
    }
}
