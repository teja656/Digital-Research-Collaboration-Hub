package com.researchsphere.service;

import com.researchsphere.dao.MilestoneDao;
import com.researchsphere.entity.Milestone;
import com.researchsphere.entity.User;

import java.time.LocalDate;
import java.util.List;

public class MilestoneService {

    private final MilestoneDao milestoneDao = new MilestoneDao();
    private final ActivityService activityService = new ActivityService();

    public List<Milestone> listAll() {
        return milestoneDao.findAll();
    }

    public List<Milestone> byProject(Long projectId) {
        return milestoneDao.findByProject(projectId);
    }

    public Milestone get(Long id) {
        return milestoneDao.findById(id);
    }

    public void create(Milestone m, User actor) {
        milestoneDao.save(m);
        activityService.log(actor, "CREATE_MILESTONE", "MILESTONE", m.getId(), m.getTitle());
    }

    public void updateProgress(Long id, int percent, String status, User actor) {
        Milestone m = milestoneDao.findById(id);
        if (m == null) return;
        m.setCompletionPercent(percent);
        if (status != null) m.setStatus(status);
        if (percent >= 100) m.setStatus("COMPLETED");
        milestoneDao.save(m);
        activityService.log(actor, "UPDATE_MILESTONE", "MILESTONE", id, "Progress " + percent + "%");
    }

    public Milestone fromForm(Long projectId, String title, String description, String targetDate, int percent) {
        Milestone m = new Milestone();
        m.setProjectId(projectId);
        m.setTitle(title);
        m.setDescription(description);
        m.setCompletionPercent(percent);
        m.setStatus(percent >= 100 ? "COMPLETED" : "IN_PROGRESS");
        if (targetDate != null && !targetDate.isEmpty()) {
            m.setTargetDate(LocalDate.parse(targetDate));
        }
        return m;
    }

    public void delete(Long id, User actor) {
        Milestone m = milestoneDao.findById(id);
        if (m != null) {
            milestoneDao.delete(m);
            activityService.log(actor, "DELETE_MILESTONE", "MILESTONE", id, "Deleted milestone");
        }
    }
}
