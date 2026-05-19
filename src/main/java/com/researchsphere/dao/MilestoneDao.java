package com.researchsphere.dao;

import com.researchsphere.entity.Milestone;

import java.util.List;

public class MilestoneDao extends GenericDao<Milestone> {

    public MilestoneDao() {
        super(Milestone.class);
    }

    public List<Milestone> findByProject(Long projectId) {
        return executeList(session -> session.createQuery(
                "from Milestone m where m.projectId = :pid order by m.targetDate", Milestone.class)
                .setParameter("pid", projectId)
                .list());
    }
}
