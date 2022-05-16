package ru.job4j.todo.controller;

import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Category;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemsService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * ItemControllerTest. Тесты  MOCKITO на контроллер отображения видов.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 04.05.2022
 */
public class ItemsControllerTest {
    private static final String ALL_ITEM = "Все задания";
    private static final String COMPLETED_ITEM = "Завершенные задания";
    private static final String NEW_ITEM = "Новые задания";

    @Test
    public void whenIndexGET() {
        User user = User.of("User", "pass");
        Item item1 = Item.of("Item1", "desc Item1", user);
        item1.setId(1);
        item1.setCreated(LocalDateTime.now().withNano(0).minusDays(5));
        item1.setDone(LocalDateTime.now().withNano(0));
        Item item2 = Item.of("Item1", "desc Item1", user);
        item2.setId(2);
        item2.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        List<Item> items = Arrays.asList(item1, item2);
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findAllItem(user)).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.index(model, false, false, session);
        verify(model).addAttribute("statusSuccess", true);
        verify(model).addAttribute("statusErr", true);
        verify(model).addAttribute("pageName", ALL_ITEM);
        verify(model).addAttribute("items", items);
        verify(model).addAttribute("user", user);
        assertThat(page, is("index"));
    }

    @Test
    public void whenDoneItemsGET() {
        User user1 = User.of("User", "pass");
        Item item1 = Item.of("Item1", "desc Item1", user1);
        item1.setId(1);
        item1.setCreated(LocalDateTime.now().withNano(0).minusDays(5));
        item1.setDone(LocalDateTime.now().withNano(0));
        List<Item> items = Arrays.asList(item1);
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findDoneItem(user1)).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user1);
        String page = itemsController.doneItems(model, session);
        verify(model).addAttribute("pageName", COMPLETED_ITEM);
        verify(model).addAttribute("items", items);
        verify(model).addAttribute("user", user1);
        assertThat(page, is("index"));
    }

    @Test
    public void whenNewItemsGET() {
        User user2 = User.of("User2", "pass2");
        Item item2 = Item.of("Item1", "desc Item1", user2);
        item2.setId(2);
        item2.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        List<Item> items = Arrays.asList(item2);
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findNewItem(user2)).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user2);
        String page = itemsController.newItems(model, session);
        verify(model).addAttribute("pageName", NEW_ITEM);
        verify(model).addAttribute("items", items);
        verify(model).addAttribute("user", user2);
        assertThat(page, is("index"));
    }

    @Test
    public void whenDetailGET() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        Set<Category> category = new CopyOnWriteArraySet<>();
        category.add(Category.of(1));
        category.add(Category.of(2));
        item.setCategory(category);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(item.getId())).thenReturn(Optional.of(item));
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.detail(model, 2, false, false, session);
        verify(model).addAttribute("statusSuccess", true);
        verify(model).addAttribute("statusErr", true);
        verify(model).addAttribute("item", item);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("categories", item.getCategory());
        assertThat(page, is("detail"));
    }

    @Test
    public void whenDetailErrGET() {
        User user = User.of("Гость", "");
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(2)).thenReturn(Optional.empty());
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.detail(model, 2, false, false, session);
        assertThat(page, is("redirect:/?statusErr=true"));
    }

    @Test
    public void whenAddItemGET() {
        User user = User.of("Гость", "");
        Model model = mock(Model.class);
        List<Category> categoryList = List.of(Category.of(1), Category.of(2));
        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.allCategory()).thenReturn(categoryList);
        ItemsService itemsService = mock(ItemsService.class);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.addItem(model, session);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("allCategory", categoryService.allCategory());
        assertThat(page, is("new"));
    }

    @Test
    public void whenCreateItemPOST() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.createItem(item, req, session);
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenEditGET() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        List<Category> categoryList = List.of(Category.of(1), Category.of(2));
        CategoryService categoryService = mock(CategoryService.class);
        when(categoryService.allCategory()).thenReturn(categoryList);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(item.getId())).thenReturn(Optional.of(item));
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        String page = itemsController.edit(model, item, session);
        verify(model).addAttribute("item", item);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("allCategory", categoryService.allCategory());
        assertThat(page, is("edit"));
    }

    @Test
    public void whenEditItemPOST() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        Set<Category> category = new CopyOnWriteArraySet<>();
        category.add(Category.of(1));
        category.add(Category.of(2));
        item.setCategory(category);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("user")).thenReturn(user);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameterValues("catId")).thenReturn(new String[]{"1", "2"});
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.updateItem(item.getId(), item)).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.editItem(item, httpSession, req);
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenEditItemPOSTErr() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        Set<Category> category = new CopyOnWriteArraySet<>();
        category.add(Category.of(1));
        category.add(Category.of(2));
        item.setCategory(category);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        HttpSession httpSession = mock(HttpSession.class);
        when(httpSession.getAttribute("user")).thenReturn(user);
        HttpServletRequest req = mock(HttpServletRequest.class);
        when(req.getParameterValues("catId")).thenReturn(new String[]{"1", "2"});
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.updateItem(item.getId(), item)).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.editItem(item, httpSession, req);
        assertThat(page, is("redirect:/?statusErr=true"));
    }

    @Test
    public void whenDoneItemPOST() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.doneItem(item.getId())).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.doneItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenDoneItemPOSTErr() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.doneItem(item.getId())).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.doneItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusErr=true"));
    }

    @Test
    public void whenDeleteItemPOST() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.deleteItem(item.getId())).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.deleteItem(item.getId());
        assertThat(page, is("redirect:/?statusSuccess=true"));
    }

    @Test
    public void whenDeleteItemPOSTErr() {
        User user = User.of("User2", "pass2");
        Item item = Item.of("Item1", "desc Item1", user);
        item.setId(2);
        item.setCreated(LocalDateTime.now().withNano(0).minusDays(10));
        Model model = mock(Model.class);
        CategoryService categoryService = mock(CategoryService.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.deleteItem(item.getId())).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService, categoryService);
        String page = itemsController.deleteItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusErr=true"));
    }
}