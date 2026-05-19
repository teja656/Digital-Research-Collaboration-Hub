package com.researchsphere.controller;

import com.researchsphere.entity.User;
import com.researchsphere.service.MilestoneService;
import com.researchsphere.service.ProjectService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/milestones")
public class MilestoneController extends BaseController {

    private final MilestoneService milestoneService = new MilestoneService();
    private final ProjectService projectService = new ProjectService();

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = currentUser(session);
        model.addAttribute("milestones", milestoneService.listAll());
        model.addAttribute("projects", projectService.listAll());
        model.addAttribute("pageTitle", "Milestones");
        model.addAttribute("activeNav", "milestones");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        return "milestones";
    }

    @PostMapping
    public String create(@RequestParam Long projectId,
                         @RequestParam String title,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) String targetDate,
                         @RequestParam(defaultValue = "0") int completionPercent,
                         HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/milestones?error=forbidden";
        }
        milestoneService.create(
                milestoneService.fromForm(projectId, title, description, targetDate, completionPercent),
                user);
        return "redirect:/app/milestones";
    }

    @PostMapping("/progress")
    public String updateProgress(@RequestParam Long id,
                                 @RequestParam int completionPercent,
                                 @RequestParam(required = false) String status,
                                 @RequestParam(required = false) String _method,
                                 HttpSession session) {
        if (_method == null || "PUT".equalsIgnoreCase(_method)) {
            milestoneService.updateProgress(id, completionPercent, status, currentUser(session));
        }
        return "redirect:/app/milestones";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/milestones?error=forbidden";
        }
        milestoneService.delete(id, user);
        return "redirect:/app/milestones";
    }
}
