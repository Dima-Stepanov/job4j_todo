package ru.job4j.todo.store;

import org.hibernate.SessionFactory;
import ru.job4j.todo.model.User;

import java.util.List;
import java.util.Optional;

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
public class HbmUsersDBStore implements Store<User> {
    private final SessionFactory sf;

    public HbmUsersDBStore(final SessionFactory sf) {
        this.sf = sf;
    }

    @Override
    public boolean create(User type) {
        return false;
    }

    @Override
    public Optional<User> findById(int id) {
        return Optional.empty();
    }

    @Override
    public boolean update(int id, User type) {
        return false;
    }

    @Override
    public boolean delete(int id) {
        return false;
    }

    @Override
    public List<User> findAll() {
        return null;
    }

    @Override
    public List<User> findNew() {
        return null;
    }

    @Override
    public List<User> findCompleted() {
        return null;
    }
}
