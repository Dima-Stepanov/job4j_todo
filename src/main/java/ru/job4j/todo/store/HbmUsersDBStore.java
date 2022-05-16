package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.User;

import java.util.Optional;
import java.util.function.Function;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.2. Mapping
 * 0. ToOne [#6873]
 * HdmUsersDBStore хранилище в базе данных модели данных Item,
 * используется hibernate.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 06.05.2022
 */
@Repository
public class HbmUsersDBStore {
    private final SessionFactory sf;

    public HbmUsersDBStore(final SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Добавление пользователя в систему.
     *
     * @param user User
     * @return boolean.
     */
    public boolean create(final User user) {
        boolean rsl = false;
        try {
            tx(session -> session.save(user));
            rsl = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * Поиск пользовотеля по id
     *
     * @param id int
     * @return Optional
     */
    public Optional<User> findById(int id) {
        return tx(session ->
                Optional.ofNullable(session.get(User.class, id))
        );
    }

    /**
     * Редактирование пользователя по
     *
     * @param id   int
     * @param user User
     * @return Optional.
     */
    public boolean update(int id, final User user) {
        boolean rsl = false;
        try {
            rsl = tx(session -> session.createQuery("update User set name=:name, password=:password "
                            + "where id=:id")
                    .setParameter("name", user.getName())
                    .setParameter("password", user.getPassword())
                    .setParameter("id", id)
                    .executeUpdate() > 0
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rsl;
    }

    /**
     * Поиск пользователя по логину и паролю.
     *
     * @param name     String.
     * @param password String.
     * @return Optional.
     */
    public Optional<User> findByNamePassword(final String name, final String password) {
        return tx(session -> session.createQuery("from User where name=:name and password=:password")
                .setParameter("name", name)
                .setParameter("password", password)
                .uniqueResultOptional());
    }

    /**
     * Шаблон проектирования WRAPPER
     *
     * @param command Function.
     * @param <T>     type
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
