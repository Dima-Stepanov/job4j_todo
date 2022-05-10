package ru.job4j.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Optional;

/**
 * 3. Мидл
 * 3.3. Hibernate
 * 3.3.1. Конфигурирование
 * 2. Создать TODO list [#3786]
 * UsersController контроллер отоброжения видов дял работы с пользователями.
 *
 * @author Dmitry Stepanov, user Dmitry
 * @since 8.05.2022
 */
@Controller
public class UsersController {
    private final UsersService service;
    private static final String IN = "Авторизация";
    private static final String REG = "Регистрация";

    public UsersController(UsersService service) {
        this.service = service;
    }

    /**
     * Метод GET вид для авторизации пользователя.
     *
     * @param model   Model
     * @param userErr Boolean
     * @param session HttpSession
     * @return String
     */
    @GetMapping("/login")
    public String login(Model model,
                        @RequestParam(value = "userErr", required = false) Boolean userErr,
                        HttpSession session) {
        model.addAttribute("user", getUserSession(session));
        model.addAttribute("userErr", userErr != null);
        return "login";
    }

    /**
     * Метод POST авторизация пользователя.
     *
     * @param user User
     * @param req  HttpServletRequest req
     * @return String
     */
    @PostMapping("/login")
    public String loginPost(@ModelAttribute User user,
                            HttpServletRequest req) {
        Optional<User> userDb = service.findByNamePassword(user.getName(), user.getPassword());
        if (userDb.isEmpty()) {
            return "redirect:/login?userErr=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", userDb.get());
        return "redirect:/";
    }

    /**
     * Метод GET вид для регистрации пользователя.
     *
     * @param model   Model
     * @param userErr Boolean
     * @param session HttpSession
     * @return String
     */
    @GetMapping("/newUser")
    public String createUser(Model model,
                             @RequestParam(value = "userErr", required = false) Boolean userErr,
                             HttpSession session) {
        model.addAttribute("user", getUserSession(session));
        model.addAttribute("userErr", userErr != null);
        return "newUser";
    }

    /**
     * Метод POST создание и авторизации пользователя.
     *
     * @param user User
     * @param req  HttServletRequest
     * @return String
     */
    @PostMapping("newUser")
    public String createUserPost(@ModelAttribute("user") User user, HttpServletRequest req) {
        if (!service.create(user)) {
            return "redirect:/newUser?userErr=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", user);
        return "redirect:/";
    }

    /**
     * Метод GET вид для редактирования пользователя.
     *
     * @param model   Model
     * @param userErr Boolean
     * @param session HttpSession
     * @return String
     */
    @GetMapping("/editUser")
    public String editUser(Model model,
                           @RequestParam(value = "userErr", required = false) Boolean userErr,
                           HttpSession session) {
        User user = getUserSession(session);
        model.addAttribute("userErr", userErr != null);
        model.addAttribute("user", user);
        model.addAttribute("editUser", user);
        return "editUser";
    }

    /**
     * Метод POST сохроняет изменение пользователя.
     *
     * @param editUser User
     * @param req      HttpServletRequest
     * @return String
     */
    @PostMapping("/editUser")
    public String editUserPost(@ModelAttribute("editUser") User editUser,
                               HttpServletRequest req) {
        if (!service.updateUser(editUser.getId(), editUser)) {
            return "redirect:/editUser/?userErr=true";
        }
        HttpSession session = req.getSession();
        session.setAttribute("user", editUser);
        return "redirect:/?statusSuccess=true";
    }

    /**
     * Выход из учетной запси пользователя.
     *
     * @param session Session
     * @return String.
     */
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
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
        }
        return user;
    }
}
