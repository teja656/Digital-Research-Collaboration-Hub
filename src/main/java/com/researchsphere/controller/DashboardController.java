package com.researchsphere.controller;

import com.researchsphere.entity.User;
import com.researchsphere.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import java.util.Map;

@Controller
@RequestMapping("/dashboard")
public class DashboardController extends BaseController {

    private final DashboardService dashboardService = new DashboardService();

    @GetMapping
    public String dashboard(HttpSession session, Model model) {
        User user = currentUser(session);
        Map<String, Object> data = dashboardService.buildDashboard(user);
        model.addAllAttributes(data);
        model.addAttribute("pageTitle", "Dashboard");
        model.addAttribute("activeNav", "dashboard");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        return "dashboard";
    }
}
