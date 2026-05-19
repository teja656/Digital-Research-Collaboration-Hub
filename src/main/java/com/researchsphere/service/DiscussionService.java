package com.researchsphere.service;

import com.researchsphere.dao.CommentDao;
import com.researchsphere.dao.DiscussionDao;
import com.researchsphere.dao.ProjectDao;
import com.researchsphere.entity.Comment;
import com.researchsphere.entity.Discussion;
import com.researchsphere.entity.Project;
import com.researchsphere.entity.User;

import java.util.List;

public class DiscussionService {

    private final DiscussionDao discussionDao = new DiscussionDao();
    private final CommentDao commentDao = new CommentDao();
    private final ProjectDao projectDao = new ProjectDao();
    private final ActivityService activityService = new ActivityService();
    private final NotificationService notificationService = new NotificationService();

    public List<Discussion> listAll() {
        return discussionDao.findAll();
    }

    public Discussion get(Long id) {
        return discussionDao.findById(id);
    }

    public List<Comment> comments(Long discussionId) {
        return commentDao.findByDiscussion(discussionId);
    }

    public List<Project> projects() {
        return projectDao.findAll();
    }

    public void createDiscussion(Discussion d, User actor) {
        discussionDao.save(d);
        activityService.log(actor, "CREATE_DISCUSSION", "DISCUSSION", d.getId(), d.getTitle());
    }

    public void addComment(Long discussionId, String content, Long parentId, User actor) {
        Comment c = new Comment();
        c.setDiscussionId(discussionId);
        c.setContent(content);
        c.setParentId(parentId);
        c.setCreatedBy(actor.getId());
        commentDao.save(c);
        activityService.log(actor, "ADD_COMMENT", "COMMENT", c.getId(), "New comment");
        Discussion d = discussionDao.findById(discussionId);
        if (d != null && !d.getCreatedBy().equals(actor.getId())) {
            notificationService.notifyUser(d.getCreatedBy(), "New Comment",
                    "New reply on: " + d.getTitle(), "COMMENT");
        }
    }

    public void deleteDiscussion(Long id, User actor) {
        Discussion d = discussionDao.findById(id);
        if (d == null) {
            return;
        }
        for (Comment c : commentDao.findByDiscussion(id)) {
            commentDao.delete(c);
        }
        discussionDao.delete(d);
        activityService.log(actor, "DELETE_DISCUSSION", "DISCUSSION", id, "Deleted discussion");
    }
}
