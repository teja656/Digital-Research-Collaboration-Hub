package com.researchsphere.util;

import com.researchsphere.entity.User;

/**
 * Role-based permissions for Admin, Faculty, and Student.
 */
public final class RoleAccess {

    private RoleAccess() {
    }

    public static boolean isAdmin(User user) {
        return user != null && "ADMIN".equalsIgnoreCase(user.getRole());
    }

    public static boolean isFaculty(User user) {
        return user != null && "FACULTY".equalsIgnoreCase(user.getRole());
    }

    public static boolean isStudent(User user) {
        return user != null && "STUDENT".equalsIgnoreCase(user.getRole());
    }

    /** Admin + Faculty: create/edit/delete projects, teams, tasks, milestones, uploads */
    public static boolean canManage(User user) {
        return isAdmin(user) || isFaculty(user);
    }

    public static boolean canAccessAdmin(User user) {
        return isAdmin(user);
    }

    /** Students may update status on tasks assigned to them */
    public static boolean canUpdateTask(User user, Long assignedTo) {
        if (user == null) {
            return false;
        }
        if (canManage(user)) {
            return true;
        }
        return isStudent(user) && assignedTo != null && assignedTo.equals(user.getId());
    }

    public static boolean canDelete(User user) {
        return canManage(user);
    }
}
