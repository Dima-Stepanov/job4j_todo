package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Item;

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
    private static final Logger LOG = LoggerFactory.getLogger(HbmItemDBStore.class.getName());

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
        LOG.info("Начало сохранения {}", item.getName());
        Session session = sf.openSession();
        session.beginTransaction();
        session.save(item);
        Optional<Item> result = Optional.ofNullable(session.get(Item.class, item.getId()));
        session.getTransaction().commit();
        session.close();
        LOG.info("{}:{} заявка сохранена", item.getId(), item.getName());
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
        LOG.info("Начало поиска заявки {}", id);
        Session session = sf.openSession();
        session.beginTransaction();
        Optional<Item> result = Optional.ofNullable(session.get(Item.class, id));
        session.getTransaction().commit();
        session.close();
        LOG.info("Результат поиска по {}:{}", id, result.get().getName());
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
        LOG.info("Начало обновление заявки {}", id);
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("update Item set name = :nameItem, "
                        + "description = :descriptionItem, created = :createdItem, "
                        + "done = :doneItem where id = :idItem")
                .setParameter("nameItem", item.getName())
                .setParameter("descriptionItem", item.getDescription())
                .setParameter("createdItem", item.getCreated())
                .setParameter("doneItem", item.getDone())
                .setParameter("idItem", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        LOG.info("Результат обновления заявки {}", result > 0);
        return result > 0;
    }

    /**
     * Удаление заявки по id
     *
     * @param id int
     * @return boolean
     */
    @Override
    public boolean delete(int id) {
        LOG.info("Начало удаления заявки {}", id);
        Session session = sf.openSession();
        session.beginTransaction();
        int result = session.createQuery("delete from Item where id = :idItem")
                .setParameter("idItem", id)
                .executeUpdate();
        session.getTransaction().commit();
        session.close();
        LOG.info("Результат удаления заявки {}", result > 0);
        return result > 0;
    }

    /**
     * Поиск всех заявок.
     *
     * @return List
     */
    @Override
    public List<Item> findAll() {
        LOG.info("Начало поиска всех заявок");
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item")
                .list();
        session.getTransaction().commit();
        session.close();
        LOG.info("Найдено заявок {}", result.size());
        return result;
    }

    /**
     * Поиск незавершенных заявок.
     *
     * @return List.
     */
    @Override
    public List<Item> findNew() {
        LOG.info("Начало поиска новых заявок");
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item  where done is null")
                .list();
        session.getTransaction().commit();
        session.close();
        LOG.info("Найдено новых заявок {}", result.size());
        return result;
    }

    /**
     * Поиск завершенных заявок.
     *
     * @return List.
     */
    @Override
    public List<Item> findCompleted() {
        LOG.info("Начало поиска завершенных заявок");
        Session session = sf.openSession();
        session.beginTransaction();
        List result = session.createQuery("from Item  where done is not null")
                .list();
        session.getTransaction().commit();
        session.close();
        LOG.info("Найдено завершенных заявок {}", result.size());
        return result;
    }
}
