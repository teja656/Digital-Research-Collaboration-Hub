package com.researchsphere.servlet;

import com.researchsphere.entity.User;
import com.researchsphere.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Registration servlet – GET form, POST creates user.
 */
public class RegisterServlet extends HttpServlet {

    private final AuthService authService = new AuthService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fullName = req.getParameter("fullName");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String confirm = req.getParameter("confirmPassword");
        String role = req.getParameter("role");

        if (fullName == null || fullName.trim().length() < 2) {
            req.setAttribute("error", "Full name must be at least 2 characters.");
            forwardWithInput(req, resp, fullName, email, role);
            return;
        }
        if (password == null || !password.equals(confirm)) {
            req.setAttribute("error", "Passwords do not match.");
            forwardWithInput(req, resp, fullName, email, role);
            return;
        }

        User user = new User();
        user.setFullName(fullName.trim());
        user.setEmail(email);
        user.setRole(role != null ? role : "STUDENT");

        String error = authService.register(user, password);
        if (error != null) {
            req.setAttribute("error", error);
            forwardWithInput(req, resp, fullName, email, role);
            return;
        }

        req.setAttribute("success", "Registration successful. Please login.");
        req.getRequestDispatcher("/jsp/login.jsp").forward(req, resp);
    }

    private void forwardWithInput(HttpServletRequest req, HttpServletResponse resp,
                                  String fullName, String email, String role) throws ServletException, IOException {
        req.setAttribute("fullName", fullName);
        req.setAttribute("email", email);
        req.setAttribute("role", role);
        req.getRequestDispatcher("/jsp/register.jsp").forward(req, resp);
    }
}
