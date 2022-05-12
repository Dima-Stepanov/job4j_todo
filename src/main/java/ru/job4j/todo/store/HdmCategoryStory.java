package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.stereotype.Repository;
import ru.job4j.todo.model.Category;

import javax.management.Query;
import java.util.List;
import java.util.Queue;
import java.util.function.Function;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.2. Mapping
 * 4. Категории в TODO List [#331991]
 * HdmCategoryStore хранилище данных категории заявки.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 12.05.2022
 */
@Repository
public class HdmCategoryStory {
    private final SessionFactory sf;

    public HdmCategoryStory(SessionFactory sf) {
        this.sf = sf;
    }

    /**
     * Возвращает список всех категорий.
     *
     * @return
     */
    public List<Category> findAll() {
        return tx(session -> session.createQuery("from Category")
                .list()
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
