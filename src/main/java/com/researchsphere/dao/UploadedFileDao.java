package com.researchsphere.dao;

import com.researchsphere.entity.UploadedFile;

import java.util.List;

public class UploadedFileDao extends GenericDao<UploadedFile> {

    public UploadedFileDao() {
        super(UploadedFile.class);
    }

    public List<UploadedFile> findByProject(Long projectId) {
        return executeList(session -> session.createQuery(
                "from UploadedFile f where f.projectId = :pid order by f.uploadedAt desc", UploadedFile.class)
                .setParameter("pid", projectId)
                .list());
    }
}
