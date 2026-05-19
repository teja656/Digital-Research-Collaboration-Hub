package com.researchsphere.dao;

import com.researchsphere.entity.ActivityLog;

import java.util.List;

public class ActivityLogDao extends GenericDao<ActivityLog> {

    public ActivityLogDao() {
        super(ActivityLog.class);
    }

    public List<ActivityLog> findRecent(int limit) {
        return executeList(session -> session.createQuery(
                "from ActivityLog a order by a.createdAt desc", ActivityLog.class)
                .setMaxResults(limit)
                .list());
    }

    public List<ActivityLog> findByUser(Long userId) {
        return executeList(session -> session.createQuery(
                "from ActivityLog a where a.userId = :uid order by a.createdAt desc", ActivityLog.class)
                .setParameter("uid", userId)
                .list());
    }
}
