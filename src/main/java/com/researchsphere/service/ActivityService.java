package com.researchsphere.service;

import com.researchsphere.dao.ActivityLogDao;
import com.researchsphere.entity.ActivityLog;
import com.researchsphere.entity.User;

import java.util.List;

public class ActivityService {

    private final ActivityLogDao activityLogDao = new ActivityLogDao();

    public void log(User user, String action, String entityType, Long entityId, String details) {
        ActivityLog log = new ActivityLog();
        log.setUserId(user != null ? user.getId() : null);
        log.setAction(action);
        log.setEntityType(entityType);
        log.setEntityId(entityId);
        log.setDetails(details);
        activityLogDao.save(log);
    }

    public List<ActivityLog> recent(int limit) {
        return activityLogDao.findRecent(limit);
    }

    public List<ActivityLog> byUser(Long userId) {
        return activityLogDao.findByUser(userId);
    }
}
