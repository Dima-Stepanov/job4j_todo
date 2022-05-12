package ru.job4j.todo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * Item модель данных.
 * 3.3.2. Mapping
 * 0. ToOne [#6873]
 * Отношение ManyToOn Item->User.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@Entity
@Table(name = "items")
public class Item implements Comparable<Item>, Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    private LocalDateTime created = LocalDateTime.now().withNano(0);
    private LocalDateTime done;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Transient
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss");
    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Category> categories = new CopyOnWriteArrayList<>();

    public static Item of(String name, String description, User user) {
        Item item = new Item();
        item.name = name;
        item.description = description;
        item.user = user;
        return item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public String getCreatedFormat() {
        return formatter.format(created);
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getDone() {
        return done;
    }

    public String getDoneFormat() {
        return formatter.format(done);
    }

    public void setDone(LocalDateTime done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Item item = (Item) o;
        return id == item.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return String.format("id: %s, name: %s, description: %s, create: %s, done %s",
                id, name, description, user,
                formatter.format(created),
                formatter.format(done));
    }

    @Override
    public int compareTo(Item item) {
        return Integer.compare(id, item.id);
    }
}
