package ru.job4j.todo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * Item модель данных.
 * 3.3.2. Mapping
 * 0. ToOne [#6873]
 * Отношение ManyToOn Item->User.
 * 3.3.2. Mapping
 * 4. Категории в TODO List [#331991]
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@Entity
@Table(name = "items")
public class Item implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String name;
    private String description;
    @Temporal(TemporalType.TIMESTAMP)
    private Date created = new Date(System.currentTimeMillis());
    @Temporal(TemporalType.TIMESTAMP)
    private Date done;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Transient
    private final DateFormat formatter = new SimpleDateFormat("dd-MMMM-yyyy HH:mm:ss");
    @ManyToMany(cascade = {CascadeType.MERGE})
    private Set<Category> category = new CopyOnWriteArraySet<>();

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

    public Date getCreated() {
        return created;
    }

    public String getCreatedFormat() {
        return formatter.format(created);
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public Date getDone() {
        return done;
    }

    public String getDoneFormat() {
        return formatter.format(done);
    }

    public void setDone(Date done) {
        this.done = done;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Set<Category> getCategory() {
        return category;
    }

    public void setCategory(Set<Category> category) {
        this.category = category;
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
}
