package com.researchsphere.controller;

import com.researchsphere.entity.Project;
import com.researchsphere.entity.Team;
import com.researchsphere.entity.UploadedFile;
import com.researchsphere.entity.User;
import com.researchsphere.service.FileService;
import com.researchsphere.service.ProjectService;
import com.researchsphere.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/projects")
public class ProjectController extends BaseController {

    private final ProjectService projectService = new ProjectService();
    private final TeamService teamService = new TeamService();
    private final FileService fileService = new FileService();

    @GetMapping
    public String list(Model model, HttpSession session,
                       @RequestParam(required = false) String uploaded,
                       @RequestParam(required = false) String error) {
        User user = currentUser(session);
        List<Project> projects = projectService.listAll();
        Map<Long, String> teamNames = new HashMap<>();
        for (Team t : teamService.listAll()) {
            teamNames.put(t.getId(), t.getName());
        }
        model.addAttribute("projects", projects);
        model.addAttribute("teams", teamService.listAll());
        model.addAttribute("teamNames", teamNames);
        model.addAttribute("pageTitle", "Research Projects");
        model.addAttribute("activeNav", "projects");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        if (uploaded != null) {
            model.addAttribute("success", "File uploaded successfully.");
        }
        if (error != null) {
            model.addAttribute("error", "forbidden".equals(error)
                    ? "Students can view projects only. Faculty/Admin can create and upload."
                    : "File upload failed. Check file type and try again.");
        }
        return "projects";
    }

    @PostMapping
    public String create(@RequestParam String title,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) String category,
                         @RequestParam(required = false) String status,
                         @RequestParam(required = false) Long teamId,
                         @RequestParam(required = false) String startDate,
                         @RequestParam(required = false) String endDate,
                         HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/projects?error=forbidden";
        }
        Project p = projectService.fromForm(title, description, category, status, teamId, user.getId(), startDate, endDate);
        projectService.create(p, user);
        return "redirect:/app/projects";
    }

    @PostMapping("/update")
    public String updatePut(@RequestParam Long id,
                            @RequestParam String title,
                            @RequestParam(required = false) String description,
                            @RequestParam(required = false) String category,
                            @RequestParam(required = false) String status,
                            @RequestParam(required = false) Long teamId,
                            @RequestParam(required = false) String startDate,
                            @RequestParam(required = false) String endDate,
                            @RequestParam(required = false) String _method,
                            HttpSession session) {
        if (!"PUT".equalsIgnoreCase(_method)) {
            return "redirect:/app/projects";
        }
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/projects?error=forbidden";
        }
        Project p = projectService.get(id);
        if (p == null) {
            return "redirect:/app/projects";
        }
        p.setTitle(title);
        p.setDescription(description);
        p.setCategory(category);
        p.setStatus(status);
        p.setTeamId(teamId);
        if (startDate != null && !startDate.isEmpty()) {
            p.setStartDate(LocalDate.parse(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            p.setEndDate(LocalDate.parse(endDate));
        }
        projectService.update(p, user);
        return "redirect:/app/projects";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/projects?error=forbidden";
        }
        projectService.delete(id, user);
        return "redirect:/app/projects";
    }

    @GetMapping("/files")
    public String files(@RequestParam Long projectId, Model model, HttpSession session) {
        User user = currentUser(session);
        List<UploadedFile> files = fileService.byProject(projectId);
        Project project = projectService.get(projectId);
        model.addAttribute("files", files);
        model.addAttribute("projectId", projectId);
        model.addAttribute("projectTitle", project != null ? project.getTitle() : "Project");
        model.addAttribute("pageTitle", "Project Files");
        model.addAttribute("activeNav", "projects");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        return "project-files";
    }

    @PostMapping("/files/delete")
    public String deleteFile(@RequestParam Long id,
                             @RequestParam Long projectId,
                             HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/projects/files?projectId=" + projectId + "&error=forbidden";
        }
        fileService.delete(id, user);
        return "redirect:/app/projects/files?projectId=" + projectId;
    }
}
