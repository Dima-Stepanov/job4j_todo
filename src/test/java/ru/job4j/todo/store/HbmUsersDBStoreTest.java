package ru.job4j.todo.store;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.job4j.todo.TodoApplication;
import ru.job4j.todo.model.User;

import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * HdmUserDBStoreTest тестирование возвращаемых параметров.
 * Тестирование поверхностное и не захватывает исключительные ситуации.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 8.05.2022
 */
public class HbmUsersDBStoreTest {
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
        session.createQuery("delete User").executeUpdate();
        session.getTransaction().commit();
        session.close();
    }

    @Test
    public void whenCreateUser() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        assertEquals(user, userStore.findByNamePassword(user.getName(), user.getPassword()).get());
    }

    @Test
    public void whenCreateUserThenFalse() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        User user1 = User.of("User", "p");
        assertFalse(userStore.create(user1));
    }

    @Test
    public void whenFindByIdUserThenPresent() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        User userInDb = userStore.findById(user.getId()).get();
        assertThat(user.getName(), is(userInDb.getName()));
    }

    @Test
    public void whenFindByIdUserThenIsEmpty() {
        HbmUsersDBStore usersDBStore = new HbmUsersDBStore(sf);
        Optional<User> result = usersDBStore.findById(-1);
        assertTrue(result.isEmpty());
    }

    @Test
    public void whenUpdateUserThenTrue() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        user.setName("User U.");
        user.setPassword("123");
        assertTrue(userStore.update(user.getId(), user));
    }

    @Test
    public void whenUpdateUserThenFalse() {
        User user = User.of("User", "pass");
        User user1 = User.of("User U.", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        userStore.create(user1);
        user.setName("User U.");
        user.setPassword("123");
        assertFalse(userStore.update(user.getId(), user));
    }

    @Test
    public void whenFindByNamePasswordUser() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        Optional<User> result = userStore.findByNamePassword(user.getName(), user.getPassword());
        assertEquals(user, result.get());
    }

    @Test
    public void whenFindByNamePasswordUserThenIsEmpty() {
        User user = User.of("User", "pass");
        HbmUsersDBStore userStore = new HbmUsersDBStore(sf);
        userStore.create(user);
        user.setPassword("123");
        Optional<User> result = userStore.findByNamePassword(user.getName(), user.getPassword());
        assertTrue(result.isEmpty());
    }
}