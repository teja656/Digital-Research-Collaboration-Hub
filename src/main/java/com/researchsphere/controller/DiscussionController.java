package com.researchsphere.controller;

import com.researchsphere.entity.Discussion;
import com.researchsphere.entity.User;
import com.researchsphere.service.DiscussionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/discussions")
public class DiscussionController extends BaseController {

    private final DiscussionService discussionService = new DiscussionService();

    @GetMapping
    public String list(Model model, HttpSession session,
                       @RequestParam(required = false) Long view) {
        User user = currentUser(session);
        model.addAttribute("discussions", discussionService.listAll());
        model.addAttribute("projects", discussionService.projects());
        model.addAttribute("pageTitle", "Discussions");
        model.addAttribute("activeNav", "discussions");
        model.addAttribute("currentUser", user);
        addRoleFlags(model, user);
        if (view != null) {
            model.addAttribute("selectedDiscussion", discussionService.get(view));
            model.addAttribute("comments", discussionService.comments(view));
        }
        return "discussions";
    }

    @PostMapping
    public String create(@RequestParam Long projectId,
                         @RequestParam String title,
                         @RequestParam String content,
                         HttpSession session) {
        User user = currentUser(session);
        Discussion d = new Discussion();
        d.setProjectId(projectId);
        d.setTitle(title);
        d.setContent(content);
        d.setCreatedBy(user.getId());
        discussionService.createDiscussion(d, user);
        return "redirect:/app/discussions";
    }

    @PostMapping("/comment")
    public String comment(@RequestParam Long discussionId,
                          @RequestParam String content,
                          @RequestParam(required = false) Long parentId,
                          HttpSession session) {
        discussionService.addComment(discussionId, content, parentId, currentUser(session));
        return "redirect:/app/discussions?view=" + discussionId;
    }

    @PostMapping("/delete")
    public String delete(@RequestParam Long id, HttpSession session) {
        User user = currentUser(session);
        if (!canManage(user)) {
            return "redirect:/app/discussions?error=forbidden";
        }
        discussionService.deleteDiscussion(id, user);
        return "redirect:/app/discussions";
    }
}
