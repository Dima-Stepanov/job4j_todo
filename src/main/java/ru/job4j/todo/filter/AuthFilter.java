package ru.job4j.todo.filter;

import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthFilter implements Filter {
    private static final String LOGIN_PAGE = "login";
    private static final String NEW_USER_PAGE = "newUser";

    /**
     * Фильтр для доступа на сервис авторизованных пользователей.
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @param chain    FilterChain
     * @throws IOException      Exception
     * @throws ServletException Exception
     */
    @Override
    public void doFilter(
            ServletRequest request,
            ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;
        String uri = req.getRequestURI();
        if (uri.endsWith(LOGIN_PAGE) || uri.endsWith(NEW_USER_PAGE)) {
            chain.doFilter(req, res);
            return;
        }
        if (req.getSession().getAttribute("user") == null) {
            res.sendRedirect(req.getContextPath() + "/" + LOGIN_PAGE);
            return;
        }
        chain.doFilter(req, res);
    }
}
