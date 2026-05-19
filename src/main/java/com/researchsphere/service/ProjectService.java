package com.researchsphere.service;

import com.researchsphere.dao.ProjectDao;
import com.researchsphere.entity.Project;
import com.researchsphere.entity.User;

import java.time.LocalDate;
import java.util.List;

public class ProjectService {

    private final ProjectDao projectDao = new ProjectDao();
    private final ActivityService activityService = new ActivityService();
    private final NotificationService notificationService = new NotificationService();

    public List<Project> listAll() {
        return projectDao.findAll();
    }

    public Project get(Long id) {
        return projectDao.findById(id);
    }

    public void create(Project project, User actor) {
        projectDao.save(project);
        activityService.log(actor, "CREATE_PROJECT", "PROJECT", project.getId(), "Created project: " + project.getTitle());
    }

    public void update(Project project, User actor) {
        projectDao.save(project);
        activityService.log(actor, "UPDATE_PROJECT", "PROJECT", project.getId(), "Updated project: " + project.getTitle());
    }

    public void delete(Long id, User actor) {
        Project p = projectDao.findById(id);
        if (p != null) {
            projectDao.delete(p);
            activityService.log(actor, "DELETE_PROJECT", "PROJECT", id, "Deleted project");
        }
    }

    public Project fromForm(String title, String description, String category, String status,
                           Long teamId, Long createdBy, String startDate, String endDate) {
        Project p = new Project();
        p.setTitle(title);
        p.setDescription(description);
        p.setCategory(category);
        p.setStatus(status != null ? status : "PLANNING");
        p.setTeamId(teamId);
        p.setCreatedBy(createdBy);
        if (startDate != null && !startDate.isEmpty()) {
            p.setStartDate(LocalDate.parse(startDate));
        }
        if (endDate != null && !endDate.isEmpty()) {
            p.setEndDate(LocalDate.parse(endDate));
        }
        return p;
    }
}
