package com.researchsphere.service;

import com.researchsphere.dao.UploadedFileDao;
import com.researchsphere.entity.UploadedFile;
import com.researchsphere.entity.User;

import java.util.List;

public class FileService {

    private final UploadedFileDao uploadedFileDao = new UploadedFileDao();
    private final ActivityService activityService = new ActivityService();

    public List<UploadedFile> byProject(Long projectId) {
        return uploadedFileDao.findByProject(projectId);
    }

    public UploadedFile get(Long id) {
        return uploadedFileDao.findById(id);
    }

    public void saveRecord(UploadedFile file, User actor) {
        uploadedFileDao.save(file);
        activityService.log(actor, "UPLOAD_FILE", "FILE", file.getId(), file.getFileName());
    }

    public void delete(Long id, User actor) {
        UploadedFile file = uploadedFileDao.findById(id);
        if (file != null) {
            uploadedFileDao.delete(file);
            activityService.log(actor, "DELETE_FILE", "FILE", id, "Deleted file: " + file.getFileName());
        }
    }
}
