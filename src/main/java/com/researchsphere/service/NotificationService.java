package com.researchsphere.service;

import com.researchsphere.dao.NotificationDao;
import com.researchsphere.entity.Notification;

import java.util.List;

public class NotificationService {

    private final NotificationDao notificationDao = new NotificationDao();

    public void notifyUser(Long userId, String title, String message, String type) {
        Notification n = new Notification();
        n.setUserId(userId);
        n.setTitle(title);
        n.setMessage(message);
        n.setType(type);
        n.setReadFlag(false);
        notificationDao.save(n);
    }

    public List<Notification> forUser(Long userId) {
        return notificationDao.findByUser(userId);
    }

    public long unreadCount(Long userId) {
        return notificationDao.countUnread(userId);
    }

    public void markRead(Long id) {
        Notification n = notificationDao.findById(id);
        if (n != null) {
            n.setReadFlag(true);
            notificationDao.save(n);
        }
    }
}
