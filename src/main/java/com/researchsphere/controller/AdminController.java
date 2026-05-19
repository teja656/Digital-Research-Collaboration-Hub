package com.researchsphere.controller;

import com.researchsphere.entity.User;
import com.researchsphere.service.ActivityService;
import com.researchsphere.service.AuthService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
public class AdminController extends BaseController {

    private final AuthService authService = new AuthService();
    private final ActivityService activityService = new ActivityService();

    @GetMapping
    public String admin(HttpSession session, Model model) {
        User user = currentUser(session);
        if (!"ADMIN".equals(user.getRole())) {
            return "redirect:/app/dashboard";
        }
        model.addAttribute("users", authService.allUsers());
        model.addAttribute("activities", activityService.recent(50));
        model.addAttribute("pageTitle", "Admin Panel");
        model.addAttribute("activeNav", "admin");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        return "admin";
    }
}
