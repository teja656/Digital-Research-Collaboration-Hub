package com.researchsphere.dao;

import com.researchsphere.entity.Comment;

import java.util.List;

public class CommentDao extends GenericDao<Comment> {

    public CommentDao() {
        super(Comment.class);
    }

    public List<Comment> findByDiscussion(Long discussionId) {
        return executeList(session -> session.createQuery(
                "from Comment c where c.discussionId = :did order by c.createdAt", Comment.class)
                .setParameter("did", discussionId)
                .list());
    }
}
