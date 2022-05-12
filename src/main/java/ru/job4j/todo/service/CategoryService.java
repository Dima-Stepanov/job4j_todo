package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.HdmCategoryStory;

import java.util.List;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.2. Mapping
 * 4. Категории в TODO List [#331991]
 * CategoryService слой бизнес логики.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 13.05.2022
 */
@Service
public class CategoryService {
    private final HdmCategoryStory story;

    public CategoryService(HdmCategoryStory story) {
        this.story = story;
    }

    public List<Category> allCategory() {
        return story.findAll();
    }
}
