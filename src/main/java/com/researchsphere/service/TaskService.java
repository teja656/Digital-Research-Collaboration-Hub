package com.researchsphere.service;

import com.researchsphere.dao.TaskDao;
import com.researchsphere.entity.Task;
import com.researchsphere.entity.User;
import com.researchsphere.util.RoleAccess;

import java.time.LocalDate;
import java.util.List;

public class TaskService {

    private final TaskDao taskDao = new TaskDao();
    private final ActivityService activityService = new ActivityService();
    private final NotificationService notificationService = new NotificationService();

    public List<Task> listAll() {
        return taskDao.findAll();
    }

    public List<Task> listForUser(User user) {
        if (user == null) {
            return listAll();
        }
        if (RoleAccess.canManage(user)) {
            return listAll();
        }
        return taskDao.findByAssignee(user.getId());
    }

    public List<Task> byProject(Long projectId) {
        return taskDao.findByProject(projectId);
    }

    public Task get(Long id) {
        return taskDao.findById(id);
    }

    public void create(Task task, User actor) {
        taskDao.save(task);
        activityService.log(actor, "CREATE_TASK", "TASK", task.getId(), "Created task: " + task.getTitle());
        if (task.getAssignedTo() != null) {
            notificationService.notifyUser(task.getAssignedTo(), "Task Assigned",
                    "You were assigned: " + task.getTitle(), "TASK");
        }
    }

    public void update(Task task, User actor) {
        taskDao.save(task);
        activityService.log(actor, "UPDATE_TASK", "TASK", task.getId(), "Updated task: " + task.getTitle());
    }

    /** Simulated PUT update for REST-style forms */
    public void updatePut(Long id, String title, String status, String priority, String dueDate, User actor) {
        Task task = taskDao.findById(id);
        if (task == null) {
            return;
        }
        if (!RoleAccess.canUpdateTask(actor, task.getAssignedTo())) {
            return;
        }
        if (RoleAccess.canManage(actor)) {
            if (title != null && !title.isEmpty()) {
                task.setTitle(title);
            }
            if (priority != null) {
                task.setPriority(priority);
            }
            if (dueDate != null && !dueDate.isEmpty()) {
                task.setDueDate(LocalDate.parse(dueDate));
            }
        }
        if (status != null) {
            task.setStatus(status);
        }
        update(task, actor);
    }

    public void delete(Long id, User actor) {
        Task t = taskDao.findById(id);
        if (t != null) {
            taskDao.delete(t);
            activityService.log(actor, "DELETE_TASK", "TASK", id, "Deleted task");
        }
    }

    public Task fromForm(Long projectId, String title, String description, Long assignedTo,
                         String priority, String status, String dueDate, Long createdBy) {
        Task t = new Task();
        t.setProjectId(projectId);
        t.setTitle(title);
        t.setDescription(description);
        t.setAssignedTo(assignedTo);
        t.setPriority(priority != null ? priority : "MEDIUM");
        t.setStatus(status != null ? status : "TODO");
        t.setCreatedBy(createdBy);
        if (dueDate != null && !dueDate.isEmpty()) {
            t.setDueDate(LocalDate.parse(dueDate));
        }
        return t;
    }
}
