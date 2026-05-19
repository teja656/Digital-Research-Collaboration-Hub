package com.researchsphere.controller;

import com.researchsphere.entity.Team;
import com.researchsphere.entity.TeamMember;
import com.researchsphere.entity.User;
import com.researchsphere.service.AuthService;
import com.researchsphere.service.TeamService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/teams")
public class TeamController extends BaseController {

    private final TeamService teamService = new TeamService();
    private final AuthService authService = new AuthService();

    @GetMapping
    public String list(Model model, HttpSession session) {
        User user = currentUser(session);
        List<Team> teams = teamService.listAll();
        Map<Long, List<TeamMember>> membersMap = new HashMap<>();
        Map<Long, String> userNames = new HashMap<>();
        for (User u : teamService.allUsers()) {
            userNames.put(u.getId(), u.getFullName());
        }
        for (Team t : teams) {
            membersMap.put(t.getId(), teamService.members(t.getId()));
        }
        model.addAttribute("teams", teams);
        model.addAttribute("membersMap", membersMap);
        model.addAttribute("userNames", userNames);
        model.addAttribute("users", teamService.allUsers());
        model.addAttribute("pageTitle", "Teams");
        model.addAttribute("activeNav", "teams");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        return "teams";
    }

    @PostMapping
    public String create(@RequestParam String name,
                         @RequestParam(required = false) String description,
                         @RequestParam Long leaderId,
                         HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/teams?error=forbidden";
        }
        Team team = new Team();
        team.setName(name);
        team.setDescription(description);
        team.setLeaderId(leaderId);
        teamService.createTeam(team, user);
        return "redirect:/app/teams";
    }

    @PostMapping("/add-member")
    public String addMember(@RequestParam Long teamId,
                            @RequestParam Long userId,
                            @RequestParam(required = false) String memberRole,
                            HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/teams?error=forbidden";
        }
        teamService.addMember(teamId, userId, memberRole, user);
        return "redirect:/app/teams";
    }

    @PostMapping("/remove-member")
    public String removeMember(@RequestParam Long teamId,
                               @RequestParam Long userId,
                               HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/teams?error=forbidden";
        }
        teamService.removeMember(teamId, userId, user);
        return "redirect:/app/teams";
    }

    @PostMapping("/update")
    public String update(@RequestParam Long id,
                         @RequestParam String name,
                         @RequestParam(required = false) String description,
                         @RequestParam(required = false) Long leaderId,
                         @RequestParam(required = false) String _method,
                         HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/teams?error=forbidden";
        }
        if (_method == null || "PUT".equalsIgnoreCase(_method)) {
            teamService.updateTeam(id, name, description, leaderId, user);
        }
        return "redirect:/app/teams";
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/teams?error=forbidden";
        }
        teamService.deleteTeam(id, user);
        return "redirect:/app/teams";
    }
}
