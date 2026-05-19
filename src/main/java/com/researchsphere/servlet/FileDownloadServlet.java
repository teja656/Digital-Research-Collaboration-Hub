package com.researchsphere.servlet;

import com.researchsphere.entity.UploadedFile;
import com.researchsphere.service.FileService;
import com.researchsphere.util.UploadPathUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class FileDownloadServlet extends HttpServlet {

    private final FileService fileService = new FileService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String idParam = req.getParameter("id");
        if (idParam == null || idParam.isBlank()) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        UploadedFile file = fileService.get(Long.parseLong(idParam.trim()));
        if (file == null) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Path physical = UploadPathUtil.resolveStoredFile(getServletContext(), file.getStoredName());
        if (!Files.isRegularFile(physical)) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "File not found on disk");
            return;
        }
        String contentType = file.getFileType() != null ? file.getFileType() : "application/octet-stream";
        resp.setContentType(contentType);
        resp.setContentLengthLong(Files.size(physical));
        resp.setHeader("Content-Disposition", "attachment; filename=\"" + file.getFileName() + "\"");
        try (InputStream in = Files.newInputStream(physical); OutputStream out = resp.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int read;
            while ((read = in.read(buffer)) != -1) {
                out.write(buffer, 0, read);
            }
        }
    }
}
