package ru.job4j.todo.controller;

import org.junit.jupiter.api.Test;
import org.springframework.ui.Model;
import ru.job4j.todo.model.User;
import ru.job4j.todo.service.UsersService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class UsersControllerTest {

    @Test
    public void whenLoginGet() {
        User user = User.of("Гость", "");
        user.setId(-1);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        UsersService usersService = mock(UsersService.class);
        UsersController usersController = new UsersController(usersService);
        String page = usersController.login(model, false, session);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("userErr", true);
        assertEquals(page, "login");
    }

    @Test
    public void whenLoginPost() {
        User user = User.of("User", "pass");
        user.setId(1);
        UsersService usersService = mock(UsersService.class);
        when(usersService.findByNamePassword(user.getName(), user.getPassword())).thenReturn(Optional.of(user));
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        UsersController usersController = new UsersController(usersService);
        String page = usersController.loginPost(user, request);
        assertEquals(page, "redirect:/");
    }

    @Test
    public void whenLoginPostErr() {
        User user = User.of("User", "pass");
        UsersService usersService = mock(UsersService.class);
        when(usersService.findByNamePassword("", "")).thenReturn(Optional.empty());
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        UsersController usersController = new UsersController(usersService);
        String page = usersController.loginPost(user, request);
        assertEquals(page, "redirect:/login?userErr=true");
    }

    @Test
    public void whenCreateUserGet() {
        Model model = mock(Model.class);
        User user = User.of("Гость", "");
        user.setId(-1);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        UsersService service = mock(UsersService.class);
        UsersController usersController = new UsersController(service);
        String page = usersController.createUser(model, false, session);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("userErr", true);
        assertEquals(page, "newUser");
    }

    @Test
    public void whenCreateUserPost() {
        User user = User.of("User", "pass");
        user.setId(1);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        UsersService service = mock(UsersService.class);
        when(service.create(user)).thenReturn(true);
        when(service.findByNamePassword(user.getName(), user.getPassword())).thenReturn(Optional.of(user));
        UsersController usersController = new UsersController(service);
        String page = usersController.createUserPost(user, request);
        assertEquals(page, "redirect:/");
    }

    @Test
    public void whenCreateUserPostErr() {
        User user = User.of("User", "pass");
        HttpServletRequest request = mock(HttpServletRequest.class);
        UsersService service = mock(UsersService.class);
        when(service.create(user)).thenReturn(false);
        when(service.findByNamePassword(user.getName(), user.getPassword())).thenReturn(Optional.of(user));
        UsersController usersController = new UsersController(service);
        String page = usersController.createUserPost(user, request);
        assertEquals(page, "redirect:/newUser?userErr=true");
    }

    @Test
    public void whenEditUserGet() {
        User user = User.of("User", "pass");
        user.setId(1);
        Model model = mock(Model.class);
        HttpSession session = mock(HttpSession.class);
        when(session.getAttribute("user")).thenReturn(user);
        UsersService service = mock(UsersService.class);
        when(service.findById(user.getId())).thenReturn(Optional.of(user));
        UsersController usersController = new UsersController(service);
        String page = usersController.editUser(model, false, session);
        verify(model).addAttribute("userErr", true);
        verify(model).addAttribute("user", user);
        verify(model).addAttribute("editUser", user);
        assertEquals(page, "editUser");
    }

    @Test
    public void whenEditUserPost() {
        User user = User.of("user", "pass");
        user.setId(1);
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpSession session = mock(HttpSession.class);
        when(request.getSession()).thenReturn(session);
        UsersService service = mock(UsersService.class);
        when(service.updateUser(user.getId(), user)).thenReturn(true);
        when(service.findByNamePassword(user.getName(), user.getPassword())).thenReturn(Optional.of(user));
        UsersController controller = new UsersController(service);
        String page = controller.editUserPost(user, request);
        assertEquals(page, "redirect:/?statusSuccess=true");
    }

    @Test
    public void whenEditUserPostErr() {
        User user = User.of("user", "pass");
        user.setId(1);
        HttpServletRequest request = mock(HttpServletRequest.class);
        UsersService service = mock(UsersService.class);
        when(service.updateUser(user.getId(), user)).thenReturn(false);
        UsersController controller = new UsersController(service);
        String page = controller.editUserPost(user, request);
        assertEquals(page, "redirect:/editUser/?userErr=true");
    }

    @Test
    public void whenLogout() {
        HttpSession session = mock(HttpSession.class);
        UsersService service = mock(UsersService.class);
        UsersController controller = new UsersController(service);
        String page = controller.logout(session);
        assertEquals(page, "redirect:/");
    }
}