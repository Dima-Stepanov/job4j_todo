package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.service.ItemsService;

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
    private final ItemsService service;
    private static final String ALL_ITEM = "Все задания";
    private static final String COMPLETED_ITEM = "Завершенные задания";
    private static final String NEW_ITEM = "Новые задания";


    public ItemsController(ItemsService service) {
        this.service = service;
    }

    /**
     * Отоброжение всех заданий.
     *
     * @param model Model.
     * @return "index".
     */
    @GetMapping("/")
    public String index(Model model,
                        @RequestParam(name = "statusSuccess", required = false) Boolean statusSuccess,
                        @RequestParam(name = "statusErr", required = false) Boolean statusErr) {
        model.addAttribute("statusSuccess", statusSuccess != null);
        model.addAttribute("statusErr", statusErr != null);
        model.addAttribute("pageName", ALL_ITEM);
        model.addAttribute("items", service.findAllItem());
        return "index";
    }

    /**
     * Отображение завершенных заданий.
     *
     * @param model Model.
     * @return "index".
     */
    @GetMapping("/doneItems")
    public String doneItems(Model model) {
        model.addAttribute("pageName", COMPLETED_ITEM);
        model.addAttribute("items", service.findCompletedItem());
        return "index";
    }

    /**
     * Отображение новых заданий.
     *
     * @param model Model.
     * @return "index"
     */
    @GetMapping("/newItems")
    public String newItems(Model model) {
        model.addAttribute("pageName", NEW_ITEM);
        model.addAttribute("items", service.findNewItem());
        return "index";
    }

    /**
     * Отображение деталей заданий.
     *
     * @param model Model
     * @param id    int.
     * @return String.
     */
    @GetMapping("/detail/{id}")
    public String detail(Model model,
                         @PathVariable("id") int id,
                         @RequestParam(name = "statusSuccess", required = false) Boolean statusSuccess,
                         @RequestParam(name = "statusErr", required = false) Boolean statusErr) {
        model.addAttribute("statusSuccess", statusSuccess != null);
        model.addAttribute("statusErr", statusErr != null);
        Optional<Item> item = service.findByIdItem(id);
        if (!item.isPresent()) {
            return "redirect:/?statusErr=true";
        }
        model.addAttribute("item", item.get());
        return "detail";
    }

    /**
     * Вызов вида new.html для добавления задания
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
        service.add(item);
        return "redirect:/detail/" + item.getId() + "?statusSuccess=true";
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
        if (!service.updateItem(item.getId(), item)) {
            return "redirect:/?statusErr=true";
        }
        return "redirect:/detail/" + item.getId() + "?statusSuccess=true";
    }

    /**
     * Метод POST устанавливает дату закрытия задания.
     *
     * @param id int
     * @return String
     */
    @PostMapping("/doneItem")
    public String doneItem(@ModelAttribute("id") int id) {
        if (!service.doneItem(id)) {
            return "redirect:/detail/" + id + "?statusErr=true";
        }
        return "redirect:/detail/" + id + "?statusSuccess=true";
    }

    /**
     * Метод POST удаление заявки.
     *
     * @param id int.
     * @return String
     */
    @PostMapping("/deleteItem")
    public String deleteItem(@ModelAttribute("id") int id) {
        if (!service.deleteItem(id)) {
            return "redirect:/detail/" + id + "?statusErr=true";
        }
        return "redirect:/?statusSuccess=true";
    }
}
