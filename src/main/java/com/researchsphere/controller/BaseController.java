package com.researchsphere.controller;

import com.researchsphere.entity.User;
import com.researchsphere.util.RoleAccess;
import com.researchsphere.util.SessionConstants;
import org.springframework.ui.Model;

import javax.servlet.http.HttpSession;

public abstract class BaseController {

    protected User currentUser(HttpSession session) {
        return (User) session.getAttribute(SessionConstants.SESSION_USER);
    }

    protected void addRoleFlags(Model model, User user) {
        model.addAttribute("isAdmin", RoleAccess.isAdmin(user));
        model.addAttribute("isFaculty", RoleAccess.isFaculty(user));
        model.addAttribute("isStudent", RoleAccess.isStudent(user));
        model.addAttribute("canManage", RoleAccess.canManage(user));
    }

    protected boolean canManage(User user) {
        return RoleAccess.canManage(user);
    }
}
