package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.job4j.todo.TodoApplication;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertTrue;

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

    @BeforeAll
    public static void beforeClass() {
        sf = new TodoApplication().sessionFactory();
    }

    @AfterAll
    public static void closeSf() {
        sf.close();
    }

    @AfterEach
    public void clearDB() {
        Session session = sf.openSession();
        session.beginTransaction();
        session.createQuery("delete Item").executeUpdate();
        session.createQuery("delete User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenCreateItem() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        HbmItemsDBStore itemStore = new HbmItemsDBStore(sf);
        itemStore.create(item);
        assertThat(itemStore.findById(item.getId()).get(), is(item));
    }

    @Test
    public void whenFindByIdItemThenEmpty() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        assertThat(Optional.empty(), is(store.findById(0)));
    }

    @Test
    public void whenFindByIdItemThenIsPresent() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        assertThat(Optional.of(item), is(store.findById(item.getId())));
    }

    @Test
    public void whenUpdateItemThenTrue() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        item.setName("updateItem");
        boolean result = store.update(item.getId(), item);
        assertTrue(result);
    }

    @Test
    public void whenDeleteThenTrue() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        boolean result = store.delete(item.getId());
        assertTrue(result);
    }

    @Test
    public void whenFindAllItem() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        Item item1 = Item.of("item1", "desc Item1", user);
        item1.setDone(new Date(System.currentTimeMillis()));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        List expected = List.of(item, item1);
        List result = store.findAll(user);
        assertThat(expected.toArray(), is(result.toArray()));
    }

    @Test
    public void whenFindNewItem() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        Item item1 = Item.of("item", "desc Item", user);
        Item item2 = Item.of("item", "desc Item", user);
        Item item3 = Item.of("item", "desc Item", user);
        item1.setDone(new Date(System.currentTimeMillis()));
        item3.setDone(new Date(System.currentTimeMillis()));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        store.create(item2);
        store.create(item3);
        List expected = List.of(item, item2);
        List result = store.findNew(user);
        assertThat(expected.toArray(), is(result.toArray()));
    }

    @Test
    public void whenFindDoneItem() {
        User user = User.of("user", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Item item = Item.of("item", "desc Item", user);
        Item item1 = Item.of("item", "desc Item", user);
        Item item2 = Item.of("item", "desc Item", user);
        Item item3 = Item.of("item", "desc Item", user);
        item1.setDone(new Date(System.currentTimeMillis()));
        item3.setDone(new Date(System.currentTimeMillis()));
        HbmItemsDBStore store = new HbmItemsDBStore(sf);
        store.create(item);
        store.create(item1);
        store.create(item2);
        store.create(item3);
        List expected = List.of(item1, item3);
        List result = store.findDone(user);
        assertThat(expected.toArray(), is(result.toArray()));
    }
}