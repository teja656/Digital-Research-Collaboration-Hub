package com.researchsphere.dao;

import com.researchsphere.entity.Project;

import java.util.List;

public class ProjectDao extends GenericDao<Project> {

    public ProjectDao() {
        super(Project.class);
    }

    public List<Project> findByTeam(Long teamId) {
        return executeList(session -> session.createQuery(
                "from Project p where p.teamId = :tid order by p.updatedAt desc", Project.class)
                .setParameter("tid", teamId)
                .list());
    }

    public long countByStatus(String status) {
        return executeLong(session -> session.createQuery(
                "select count(p) from Project p where p.status = :st", Long.class)
                .setParameter("st", status)
                .uniqueResult());
    }

    public long countAll() {
        return executeLong(session -> session.createQuery("select count(p) from Project p", Long.class).uniqueResult());
    }
}
