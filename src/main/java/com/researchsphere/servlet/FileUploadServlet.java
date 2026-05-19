package com.researchsphere.servlet;

import com.researchsphere.entity.UploadedFile;
import com.researchsphere.entity.User;
import com.researchsphere.service.FileService;
import com.researchsphere.service.NotificationService;
import com.researchsphere.util.RoleAccess;
import com.researchsphere.util.SessionConstants;
import com.researchsphere.util.UploadPathUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@MultipartConfig(maxFileSize = 10485760, maxRequestSize = 20971520)
public class FileUploadServlet extends HttpServlet {

    private final FileService fileService = new FileService();
    private final NotificationService notificationService = new NotificationService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendRedirect(req.getContextPath() + "/app/projects");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String ctx = req.getContextPath();
        try {
            User user = (User) req.getSession().getAttribute(SessionConstants.SESSION_USER);
            if (user == null) {
                resp.sendRedirect(ctx + "/login");
                return;
            }
            if (!RoleAccess.canManage(user)) {
                resp.sendRedirect(ctx + "/app/projects?error=forbidden");
                return;
            }

            Part filePart = req.getPart("file");
            String projectIdStr = req.getParameter("projectId");

            if (filePart == null || filePart.getSize() == 0 || projectIdStr == null || projectIdStr.isBlank()) {
                resp.sendRedirect(ctx + "/app/projects?error=upload");
                return;
            }

            String submitted = filePart.getSubmittedFileName();
            if (submitted == null || submitted.isBlank()) {
                resp.sendRedirect(ctx + "/app/projects?error=upload");
                return;
            }

            String originalName = UploadPathUtil.safeFileName(submitted);
            String storedName = UUID.randomUUID() + "_" + originalName;

            Path uploadDir = UploadPathUtil.resolveUploadDirectory(getServletContext());
            Path target = uploadDir.resolve(storedName);

            try (InputStream in = filePart.getInputStream()) {
                Files.copy(in, target, StandardCopyOption.REPLACE_EXISTING);
            }

            UploadedFile record = new UploadedFile();
            record.setProjectId(Long.parseLong(projectIdStr.trim()));
            record.setFileName(originalName);
            record.setStoredName(storedName);
            record.setFileType(filePart.getContentType());
            record.setFileSize(Files.size(target));
            record.setUploadedBy(user.getId());
            fileService.saveRecord(record, user);

            notificationService.notifyUser(user.getId(), "File Uploaded",
                    "Uploaded: " + originalName, "FILE");

            resp.sendRedirect(ctx + "/app/projects?uploaded=1");
        } catch (Exception ex) {
            getServletContext().log("File upload failed", ex);
            resp.sendRedirect(ctx + "/app/projects?error=upload");
        }
    }
}
