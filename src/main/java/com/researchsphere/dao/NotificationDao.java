package com.researchsphere.dao;

import com.researchsphere.entity.Notification;

import java.util.List;

public class NotificationDao extends GenericDao<Notification> {

    public NotificationDao() {
        super(Notification.class);
    }

    public List<Notification> findByUser(Long userId) {
        return executeList(session -> session.createQuery(
                "from Notification n where n.userId = :uid order by n.createdAt desc", Notification.class)
                .setParameter("uid", userId)
                .list());
    }

    public long countUnread(Long userId) {
        return executeLong(session -> session.createQuery(
                "select count(n) from Notification n where n.userId = :uid and n.readFlag = false", Long.class)
                .setParameter("uid", userId)
                .uniqueResult());
    }
}
