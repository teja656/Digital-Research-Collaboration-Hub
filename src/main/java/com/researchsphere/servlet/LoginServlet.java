package com.researchsphere.servlet;

import com.researchsphere.entity.User;
import com.researchsphere.service.AuthService;
import com.researchsphere.util.SessionConstants;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.io.IOException;

/**
 * Login servlet – GET shows form, POST authenticates (session + optional cookie).
 */
public class LoginServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session != null && session.getAttribute(SessionConstants.SESSION_USER) != null) {
            resp.sendRedirect(req.getContextPath() + "/app/dashboard");
            return;
        }
        Cookie[] cookies = req.getCookies();
        if (cookies != null) {
            for (Cookie c : cookies) {
                if (SessionConstants.COOKIE_REMEMBER.equals(c.getName())) {
                    req.setAttribute("rememberEmail", c.getValue());
                    break;
                }
            }
        }
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String remember = req.getParameter("rememberMe");

        if (email == null || email.trim().isEmpty() || password == null || password.isEmpty()) {
            req.setAttribute("error", "Email and password are required.");
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        User user = authService.login(email.trim(), password);
        if (user == null) {
            req.setAttribute("error", "Invalid email or password.");
            req.setAttribute("email", email);
            req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
            return;
        }

        HttpSession session = req.getSession(true);
        session.setAttribute(SessionConstants.SESSION_USER, user);
        session.setMaxInactiveInterval(30 * 60);

        if ("on".equals(remember)) {
            Cookie cookie = new Cookie(SessionConstants.COOKIE_REMEMBER, user.getEmail());
            cookie.setMaxAge(SessionConstants.COOKIE_MAX_AGE);
            cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
            cookie.setHttpOnly(true);
            resp.addCookie(cookie);
        } else {
            Cookie cookie = new Cookie(SessionConstants.COOKIE_REMEMBER, "");
            cookie.setMaxAge(0);
            cookie.setPath(req.getContextPath().isEmpty() ? "/" : req.getContextPath());
            resp.addCookie(cookie);
        }

        resp.sendRedirect(req.getContextPath() + "/app/dashboard");
    }
}
