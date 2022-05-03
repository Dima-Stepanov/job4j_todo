package ru.job4j.todo.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * Item модель данных.
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

    public Item() {
    }

    public Item(int id) {
        this.id = id;
    }

    public Item(String name) {
        this.name = name;
    }

    public Item(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public Item(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Item(int id, String name, String description, LocalDateTime created, LocalDateTime done) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.created = created;
        this.done = done;
    }

    public Item(String name, String description, LocalDateTime created, LocalDateTime done) {
        this.name = name;
        this.description = description;
        this.created = created;
        this.done = done;
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
        return DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss").format(created);
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    public LocalDateTime getDone() {
        return done;
    }

    public String getDoneFormat() {
        return DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss").format(done);
    }

    public void setDone(LocalDateTime done) {
        this.done = done;
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
                id, name, description,
                DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss").format(created),
                DateTimeFormatter.ofPattern("dd-MMMM-yyyy HH:mm:ss").format(done));
    }

    @Override
    public int compareTo(Item item) {
        return Integer.compare(id, item.id);
    }
}
