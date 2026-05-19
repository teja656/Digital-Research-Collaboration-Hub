package com.researchsphere.dao;

import com.researchsphere.entity.Team;

import java.util.List;

public class TeamDao extends GenericDao<Team> {

    public TeamDao() {
        super(Team.class);
    }

    public List<Team> findByLeader(Long leaderId) {
        return executeList(session -> session.createQuery(
                "from Team t where t.leaderId = :lid order by t.createdAt desc", Team.class)
                .setParameter("lid", leaderId)
                .list());
    }

    public long countAll() {
        return executeLong(session -> session.createQuery("select count(t) from Team t", Long.class).uniqueResult());
    }
}
