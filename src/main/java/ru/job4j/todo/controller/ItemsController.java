package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.Item;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.CategoryService;
import ru.job4j.todo.service.ItemsService;

import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * 4. Категории в TODO List [#331991]
 * ItemController контроллер отоброжения видов.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 01.05.2022
 */
@Controller
public class ItemsController {
    private final ItemsService itemsService;
    private final CategoryService categoryService;
    private static final String ALL_ITEM = "Все задания";
    private static final String COMPLETED_ITEM = "Завершенные задания";
    private static final String NEW_ITEM = "Новые задания";

    public ItemsController(ItemsService itemsService, CategoryService categoryService) {
        this.itemsService = itemsService;
        this.categoryService = categoryService;
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
                        @RequestParam(name = "statusErr", required = false) Boolean statusErr,
                        HttpSession session) {
        User user = getUserSession(session);
        model.addAttribute("user", user);
        model.addAttribute("statusSuccess", statusSuccess != null);
        model.addAttribute("statusErr", statusErr != null);
        model.addAttribute("pageName", ALL_ITEM);
        model.addAttribute("items", itemsService.findAllItem(user));
        return "index";
    }

    /**
     * Отображение завершенных заданий.
     *
     * @param model Model.
     * @return "index".
     */
    @GetMapping("/doneItems")
    public String doneItems(Model model, HttpSession session) {
        User user = getUserSession(session);
        model.addAttribute("user", user);
        model.addAttribute("pageName", COMPLETED_ITEM);
        model.addAttribute("items", itemsService.findDoneItem(user));
        return "index";
    }

    /**
     * Отображение новых заданий.
     *
     * @param model   Model.
     * @param session HttpSession
     * @return String.
     */
    @GetMapping("/newItems")
    public String newItems(Model model, HttpSession session) {
        User user = getUserSession(session);
        model.addAttribute("user", user);
        model.addAttribute("pageName", NEW_ITEM);
        model.addAttribute("items", itemsService.findNewItem(user));
        return "index";
    }

    /**
     * Отображение деталей заданий.
     *
     * @param model         Model
     * @param id            int
     * @param statusSuccess Boolean
     * @param statusErr     Boolean
     * @param session       HttpSession
     * @return String.
     */
    @GetMapping("/detail/{id}")
    public String detail(Model model,
                         @PathVariable("id") int id,
                         @RequestParam(name = "statusSuccess", required = false) Boolean statusSuccess,
                         @RequestParam(name = "statusErr", required = false) Boolean statusErr,
                         HttpSession session) {
        Optional<Item> item = itemsService.findByIdItem(id);
        User user = getUserSession(session);
        if (item.isPresent() && user.equals(item.get().getUser())) {
            model.addAttribute("user", user);
            model.addAttribute("statusSuccess", statusSuccess != null);
            model.addAttribute("statusErr", statusErr != null);
            model.addAttribute("item", item.get());
            return "detail";
        }
        return "redirect:/?statusErr=true";
    }

    /**
     * Вызов вида newItem.html для добавления задания
     *
     * @param model   Model
     * @param session HttpSession
     * @return String
     */
    @GetMapping("/new")
    public String addItem(Model model,
                          HttpSession session) {
        model.addAttribute("category", categoryService.allCategory());
        model.addAttribute("user", getUserSession(session));
        return "new";
    }

    /**
     * Метод POST добовление нового задания.
     *
     * @param item Item.
     * @return String.
     */
    @PostMapping("/createItem")
    public String createItem(@ModelAttribute("item") Item item,
                             HttpSession session) {
        item.setUser((User) session.getAttribute("user"));
        itemsService.add(item);
        return "redirect:/detail/" + item.getId() + "?statusSuccess=true";
    }

    /**
     * Метод вызывает вид редактирования задания.
     *
     * @param model   Model
     * @param item    Item
     * @param session HttpSession
     * @return String
     */
    @GetMapping("/edit")
    public String edit(Model model,
                       @ModelAttribute("item") Item item,
                       HttpSession session) {
        User user = getUserSession(session);
        Optional<Item> findItem = itemsService.findByIdItem(item.getId());
        if (findItem.isPresent() && user.equals(findItem.get().getUser())) {
            model.addAttribute("user", user);
            model.addAttribute("item", findItem.get());
            return "edit";
        }
        return "redirect:/?statusErr=true";
    }

    /**
     * Метод POST редактирования задания.
     *
     * @param item Item
     * @return String
     */
    @PostMapping("/editItem")
    public String editItem(@ModelAttribute("item") Item item) {
        if (!itemsService.updateItem(item.getId(), item)) {
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
        if (!itemsService.doneItem(id)) {
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
        if (!itemsService.deleteItem(id)) {
            return "redirect:/detail/" + id + "?statusErr=true";
        }
        return "redirect:/?statusSuccess=true";
    }

    /**
     * Метод возврощает текущего пользовотеля из HttpSession.
     *
     * @param session HttpSession
     * @return User.
     */
    private User getUserSession(HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user == null) {
            user = User.of("Гость", "");
            user.setId(-1);
        }
        return user;
    }
}