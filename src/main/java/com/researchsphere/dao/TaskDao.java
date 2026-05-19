package com.researchsphere.dao;

import com.researchsphere.entity.Task;

import java.util.List;

public class TaskDao extends GenericDao<Task> {

    public TaskDao() {
        super(Task.class);
    }

    public List<Task> findByProject(Long projectId) {
        return executeList(session -> session.createQuery(
                "from Task t where t.projectId = :pid order by t.dueDate", Task.class)
                .setParameter("pid", projectId)
                .list());
    }

    public List<Task> findByAssignee(Long userId) {
        return executeList(session -> session.createQuery(
                "from Task t where t.assignedTo = :uid order by t.dueDate", Task.class)
                .setParameter("uid", userId)
                .list());
    }

    public long countByStatus(String status) {
        return executeLong(session -> session.createQuery(
                "select count(t) from Task t where t.status = :st", Long.class)
                .setParameter("st", status)
                .uniqueResult());
    }

    public long countAll() {
        return executeLong(session -> session.createQuery("select count(t) from Task t", Long.class).uniqueResult());
    }
}
