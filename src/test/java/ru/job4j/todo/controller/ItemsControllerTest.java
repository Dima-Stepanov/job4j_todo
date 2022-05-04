package ru.job4j.todo.controller;

import org.junit.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemsService;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
        List<Item> items = Arrays.asList(
                new Item(1, "Item1", "desc Item1",
                        LocalDateTime.now().withNano(0).minusDays(5),
                        LocalDateTime.now().withNano(0)),
                new Item(2, "Item 2", "desc Item2",
                        LocalDateTime.now().withNano(0).minusDays(10), null));
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findAllItem()).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.index(model, false, false);
        verify(model).addAttribute("statusSuccess", true);
        verify(model).addAttribute("statusErr", true);
        verify(model).addAttribute("pageName", ALL_ITEM);
        verify(model).addAttribute("items", items);
        assertThat(page, is("index"));
    }

    @Test
    public void whenDoneItemsGET() {
        List<Item> items = Arrays.asList(
                new Item(1, "Item1", "desc Item1",
                        LocalDateTime.now().withNano(0).minusDays(5),
                        LocalDateTime.now().withNano(0)));
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findCompletedItem()).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.doneItems(model);
        verify(model).addAttribute("pageName", COMPLETED_ITEM);
        verify(model).addAttribute("items", items);
        assertThat(page, is("index"));
    }

    @Test
    public void whenNewItemsGET() {
        List<Item> items = Arrays.asList(
                new Item(2, "Item 2", "desc Item2",
                        LocalDateTime.now().withNano(0).minusDays(10), null));
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findNewItem()).thenReturn(items);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.newItems(model);
        verify(model).addAttribute("pageName", NEW_ITEM);
        verify(model).addAttribute("items", items);
        assertThat(page, is("index"));
    }

    @Test
    public void whenDetailGET() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0).minusDays(10), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(item.getId())).thenReturn(Optional.of(item));
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.detail(model, 2, false, false);
        verify(model).addAttribute("statusSuccess", true);
        verify(model).addAttribute("statusErr", true);
        verify(model).addAttribute("item", item);
        assertThat(page, is("detail"));
    }

    @Test
    public void whenDetailErrGET() {
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(2)).thenReturn(Optional.empty());
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.detail(model, 2, false, false);
        verify(model).addAttribute("statusSuccess", true);
        verify(model).addAttribute("statusErr", true);
        assertThat(page, is("/?statusErr=true"));
    }

    @Test
    public void whenAddItemGET() {
        Item item = new Item();
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.addItem(item);
        assertThat(page, is("new"));
    }

    @Test
    public void whenCreateItemPOST() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.add(item)).thenReturn(Optional.of(item));
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.createItem(item);
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenCreateItemPOSTErr() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.add(item)).thenReturn(Optional.empty());
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.createItem(item);
        assertThat(page, is("redirect:/?statusErr=true"));
    }

    @Test
    public void whenEditGET() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.findByIdItem(item.getId())).thenReturn(Optional.of(item));
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.edit(model, item.getId());
        verify(model).addAttribute("item", item);
        assertThat(page, is("edit"));
    }

    @Test
    public void whenEditItemPOST() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.updateItem(item.getId(), item)).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.editItem(item);
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenEditItemPOSTErr() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.updateItem(item.getId(), item)).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.editItem(item);
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusErr=true"));
    }

    @Test
    public void whenDoneItemPOST() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.doneItem(item.getId())).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.doneItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusSuccess=true"));
    }

    @Test
    public void whenDoneItemPOSTErr() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.doneItem(item.getId())).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.doneItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusErr=true"));
    }

    @Test
    public void whenDeleteItemPOST() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.deleteItem(item.getId())).thenReturn(true);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.deleteItem(item.getId());
        assertThat(page, is("redirect:/?statusSuccess=true"));
    }

    @Test
    public void whenDeleteItemPOSTErr() {
        Item item = new Item(2, "Item 2", "desc Item2",
                LocalDateTime.now().withNano(0), null);
        Model model = mock(Model.class);
        ItemsService itemsService = mock(ItemsService.class);
        when(itemsService.deleteItem(item.getId())).thenReturn(false);
        ItemsController itemsController = new ItemsController(itemsService);
        String page = itemsController.deleteItem(item.getId());
        assertThat(page, is("redirect:/detail/" + item.getId() + "?statusErr=true"));
    }
}