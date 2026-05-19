package com.researchsphere.service;

import com.researchsphere.dao.*;
import com.researchsphere.entity.ActivityLog;
import com.researchsphere.entity.Notification;
import com.researchsphere.entity.Team;
import com.researchsphere.entity.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DashboardService {

    private final ProjectDao projectDao = new ProjectDao();
    private final TaskDao taskDao = new TaskDao();
    private final TeamDao teamDao = new TeamDao();
    private final UserDao userDao = new UserDao();
    private final ActivityLogDao activityLogDao = new ActivityLogDao();
    private final NotificationDao notificationDao = new NotificationDao();

    public Map<String, Object> buildDashboard(User user) {
        Map<String, Object> data = new HashMap<>();
        data.put("projectCount", projectDao.countAll());
        data.put("taskCount", taskDao.countAll());
        data.put("teamCount", teamDao.countAll());
        data.put("userCount", userDao.countAll());
        data.put("tasksTodo", taskDao.countByStatus("TODO"));
        data.put("tasksInProgress", taskDao.countByStatus("IN_PROGRESS"));
        data.put("tasksDone", taskDao.countByStatus("DONE"));
        data.put("projectsPlanning", projectDao.countByStatus("PLANNING"));
        data.put("projectsActive", projectDao.countByStatus("IN_PROGRESS"));
        data.put("projectsCompleted", projectDao.countByStatus("COMPLETED"));
        List<ActivityLog> activities = activityLogDao.findRecent(10);
        data.put("recentActivity", activities);
        List<Notification> notifications = notificationDao.findByUser(user.getId());
        if (notifications.size() > 8) {
            notifications = notifications.subList(0, 8);
        }
        data.put("notifications", notifications);
        data.put("unreadNotifications", notificationDao.countUnread(user.getId()));
        List<Team> teams = teamDao.findAll();
        if (teams.size() > 5) {
            teams = teams.subList(0, 5);
        }
        data.put("teams", teams);
        return data;
    }
}
