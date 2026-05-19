package com.researchsphere.dao;

import com.researchsphere.entity.Discussion;

import java.util.List;

public class DiscussionDao extends GenericDao<Discussion> {

    public DiscussionDao() {
        super(Discussion.class);
    }

    public List<Discussion> findByProject(Long projectId) {
        return executeList(session -> session.createQuery(
                "from Discussion d where d.projectId = :pid order by d.createdAt desc", Discussion.class)
                .setParameter("pid", projectId)
                .list());
    }
}
