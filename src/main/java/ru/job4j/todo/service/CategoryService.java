package ru.job4j.todo.service;

import org.springframework.stereotype.Service;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.HdmCategoryDBStory;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
    private final HdmCategoryDBStory story;

    public CategoryService(HdmCategoryDBStory story) {
        this.story = story;
    }

    /**
     * Возвращает все категории из хронилища.
     *
     * @return List.
     */
    public List<Category> allCategory() {
        return story.findAll();
    }

    /**
     * Метод преобразует из массива id в Category
     *
     * @param catId String[]
     * @return Set.
     */
    public Set<Category> getSelectCategory(String[] catId) {
        if (catId != null) {
            return Arrays.stream(catId)
                    .parallel()
                    .map(c -> Category.of(Integer.parseInt(c)))
                    .collect(Collectors.toSet());
        }
        return new HashSet<>();
    }
}
