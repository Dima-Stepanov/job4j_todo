package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.todo.TodoApplication;
import ru.job4j.todo.model.Item;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
public class HbmItemDBStoreTest {
    static SessionFactory sf;

    @BeforeClass
    public static void beforeClass() {
        sf = new TodoApplication().sessionFactory();
    }

    @AfterClass
    public static void closeSf() {
        sf.close();
    }

    @After
    public void clearDB() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Item");
        session.getTransaction().commit();
        session.close();
    }


    @Test
    public void create() {
        Item item = new Item("item");
        HbmItemDBStore store = new HbmItemDBStore(sf);
        Optional<Item> result = store.create(item);
        assertThat(store.findById(item.getId()), is(result));
    }

    @Test
    public void findById() {
    }

    @Test
    public void update() {
    }

    @Test
    public void delete() {
    }

    @Test
    public void findAll() {
    }

    @Test
    public void findNew() {
    }

    @Test
    public void findCompleted() {
    }
}