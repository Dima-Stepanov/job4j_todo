package ru.job4j.todo.service;

import org.junit.jupiter.api.Test;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.store.HdmCategoryDBStory;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.2. Mapping
 * 4. Категории в TODO List [#331991]
 * CategoryServiceTest
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 20.05.2022
 */
public class CategoryServiceTest {

    @Test
    public void getSelectCategory() {
        HdmCategoryDBStory hdmCategoryDBStory = mock(HdmCategoryDBStory.class);
        CategoryService categoryService = new CategoryService(hdmCategoryDBStory);
        String[] catId = new String[]{"1", "2", "3"};
        Set<Category> expected = Set.of(Category.of(1), Category.of(2), Category.of(3));
        Set<Category> result = categoryService.getSelectCategory(catId);
        assertEquals(expected, result);
    }
}