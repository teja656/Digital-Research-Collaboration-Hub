package com.researchsphere.controller;

import com.researchsphere.entity.Task;
import com.researchsphere.entity.User;
import com.researchsphere.service.ProjectService;
import com.researchsphere.service.TaskService;
import com.researchsphere.service.TeamService;
import com.researchsphere.util.RoleAccess;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/tasks")
public class TaskController extends BaseController {

    private final TaskService taskService = new TaskService();
    private final ProjectService projectService = new ProjectService();
    private final TeamService teamService = new TeamService();

    @GetMapping
    public String list(Model model, HttpSession session,
                       @RequestParam(required = false) String error) {
        User user = currentUser(session);
        model.addAttribute("tasks", taskService.listForUser(user));
        model.addAttribute("projects", projectService.listAll());
        model.addAttribute("users", teamService.allUsers());
        model.addAttribute("pageTitle", RoleAccess.isStudent(user) ? "My Tasks" : "Tasks");
        model.addAttribute("activeNav", "tasks");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        if (error != null) {
            model.addAttribute("error", "You do not have permission for that action.");
        }
        return "tasks";
    }

    @PostMapping
    public String create(@RequestParam Long projectId,
                         @RequestParam String title,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Long assignedTo,
                         @RequestParam(required = false) String priority,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) String dueDate,
                         HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/tasks?error=forbidden";
        }
        Task t = taskService.fromForm(projectId, title, description, assignedTo, priority, status, dueDate, user.getId());
        taskService.create(t, user);
        return "redirect:/app/tasks";
    }

    @PostMapping("/put-update")
    public String putUpdate(@RequestParam Long id,
                            @RequestParam(required = false) String title,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) String priority,
                            @RequestParam(required = false) String dueDate,
                            @RequestParam String _method,
                            HttpSession session) {
        if ("PUT".equalsIgnoreCase(_method)) {
            taskService.updatePut(id, title, status, priority, dueDate, currentUser(session));
        }
        return "redirect:/app/tasks";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/tasks?error=forbidden";
        }
        taskService.delete(id, user);
        return "redirect:/app/tasks";
    }
}
