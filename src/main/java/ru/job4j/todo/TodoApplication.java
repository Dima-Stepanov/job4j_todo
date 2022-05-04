package ru.job4j.todo;

import org.hibernate.SessionFactory;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * TodoApplication содержит метод main, и объект SessionFactory
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 29.04.2022
 */
@SpringBootApplication
public class TodoApplication {

    @Bean(destroyMethod = "close")
    public SessionFactory sessionFactory() {
        final StandardServiceRegistry registry = new StandardServiceRegistryBuilder()
                .configure().build();
        return new MetadataSources(registry).buildMetadata().buildSessionFactory();

    }

    /**
     * Ловил ошибку.
     * is in unnamed module of loader org.springframework.boot.devtools.restart.classloader.RestartClassLoader
     * Исправил добавив System.setProperty("spring.devtools.restart.enabled", "false");
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        SpringApplication.run(TodoApplication.class, args);
        System.out.println("Go to http://localhost:8080/");
    }
}
