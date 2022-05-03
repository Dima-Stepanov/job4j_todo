package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemService;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * ItemController контроллер отоброжения видов.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 01.05.2022
 */
@Controller
public class ItemsController {
    private final ItemService service;
    private static final String ALL_ITEM = "Все задания";
    private static final String COMPLETED_ITEM = "Завершенные задания";
    private static final String NEW_ITEM = "Новые задания";


    public ItemsController(ItemService service) {
        this.service = service;
    }

    /**
     * Отоброжение всех заданий.
     *
     * @param model Model.
     * @return "index".
     */
    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("pageName", ALL_ITEM);
        model.addAttribute("items", service.findAllItem());
        return "index";
    }

    /**
     * Отоброжение завершенных заданий.
     *
     * @param model Model.
     * @return "index".
     */
    @GetMapping("/completedItem")
    public String completedItem(Model model) {
        model.addAttribute("pageName", COMPLETED_ITEM);
        model.addAttribute("items", service.findCompletedItem());
        return "index";
    }

    /**
     * Отоброжение новых заданий.
     *
     * @param model Model.
     * @return "index"
     */
    @GetMapping("/newItem")
    public String newItem(Model model) {
        model.addAttribute("pageName", NEW_ITEM);
        model.addAttribute("items", service.findNewItem());
        return "index";
    }

    /**
     * Отоброжение деталей заданий.
     *
     * @param model Model
     * @param id    int.
     * @return String.
     */
    @GetMapping("/detail/{id}")
    public String detail(Model model, @PathVariable("id") int id) {
        Optional<Item> itemFind = service.findByIdItem(id);
        if (itemFind.isEmpty()) {
            return "/";
        }
        model.addAttribute("item", service.findByIdItem(id).get());
        return "detail";
    }

    /**
     * Вызов вида new.html для добовления задания
     *
     * @return String
     */
    @GetMapping("/new")
    public String addItem(@ModelAttribute("item") Item item) {
        return "new";
    }

    /**
     * Метод POST добовление нового задания.
     *
     * @param item Item.
     * @return String.
     */
    @PostMapping("/createItem")
    public String createItem(@ModelAttribute("item") Item item) {
        Optional<Item> result = service.add(item);
        return "redirect:/detail/" + result.get().getId();
    }

    /**
     * Метод вызывает вид редактирования задания.
     *
     * @param model Model
     * @param id    int
     * @return String
     */
    @GetMapping("/detail/edit/{id}")
    public String edit(Model model, @PathVariable int id) {
        model.addAttribute("item", service.findByIdItem(id).get());
        return "edit";
    }

    /**
     * Метод POST редактирования задания.
     *
     * @param item Item
     * @return String
     */
    @PostMapping("/editItem")
    public String editItem(@ModelAttribute("item") Item item) {
        service.updateItem(item.getId(), item);
        return "redirect:/detail/" + item.getId();
    }

    /**
     * Метод POST устонавливает дату закрытия задания.
     *
     * @param item Item
     * @return String
     */
    @PostMapping("/doneItem")
    public String doneItem(@ModelAttribute("item") Item item) {
        item.setDone(LocalDateTime.now().withNano(0));
        service.updateItem(item.getId(), item);
        return "redirect:/detail/" + item.getId();
    }

    /**
     * Метод POST удаление заявки.
     *
     * @param item Item
     * @return String
     */
    @PostMapping("/deleteItem")
    public String deleteItem(@ModelAttribute("item") Item item) {
        service.deleteItem(item.getId());
        return "redirect:/";
    }
}
