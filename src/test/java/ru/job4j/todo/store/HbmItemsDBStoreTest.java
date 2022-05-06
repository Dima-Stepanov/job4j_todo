package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.todo.TodoApplication;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * HdmItemDBStoreTest тестирование возвращаемых параметров.
 * Тестирование поверхностное и не захватывает исключительные ситуации.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
public class HbmItemsDBStoreTest {
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
        session.createQuery("delete Item").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenCreateItem() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        assertThat(store.findById(item.getId()).get(), is(item));
    }

    @Test
    public void whenFindByIdItemThenEmpty() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        assertThat(Optional.empty(), is(store.findById(0)));
    }

    @Test
    public void whenFindByIdItemThenIsPresent() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        assertThat(Optional.of(item), is(store.findById(item.getId())));
    }

    @Test
    public void whenUpdateItemThenTrue() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        item.setName("updateItem");
        boolean result = store.update(item.getId(), item);
        assertTrue(result);
    }

    @Test
    public void whenDeleteThenTrue() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        boolean result = store.delete(item.getId());
        assertTrue(result);
    }

    @Test
    public void whenFindAllItem() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        Item item1 = Item.of("item1", "desc Item1", User.of("user1", "pass1"));
        item1.setDone(LocalDateTime.now());
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        List expected = List.of(item, item1);
        List result = store.findAll();
        assertThat(expected.toArray(), is(result.toArray()));
    }

    @Test
    public void whenFindNewItem() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        Item item1 = Item.of("item", "desc Item", User.of("user1", "pass1"));
        Item item2 = Item.of("item", "desc Item", User.of("user2", "pass2"));
        Item item3 = Item.of("item", "desc Item", User.of("user3", "pass3"));
        item1.setDone(LocalDateTime.now());
        item3.setDone(LocalDateTime.now());
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        store.create(item2);
        store.create(item3);
        List expected = List.of(item, item2);
        List result = store.findNew();
        assertThat(expected.toArray(), is(result.toArray()));
    }

    @Test
    public void whenFindCompletedItem() {
        Item item = Item.of("item", "desc Item", User.of("user", "pass"));
        Item item1 = Item.of("item", "desc Item", User.of("user1", "pass1"));
        Item item2 = Item.of("item", "desc Item", User.of("user2", "pass2"));
        Item item3 = Item.of("item", "desc Item", User.of("user3", "pass3"));
        item1.setDone(LocalDateTime.now());
        item3.setDone(LocalDateTime.now());
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        store.create(item2);
        store.create(item3);
        List expected = List.of(item1, item3);
        List result = store.findCompleted();
        assertThat(expected.toArray(), is(result.toArray()));
    }
}