package com.researchsphere.util;

import javax.servlet.ServletContext;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Resolves a stable upload directory for Jetty/Tomcat (getRealPath may be null or under target/tmp).
 */
public final class UploadPathUtil {

    private static final String SUB_DIR = "researchsphere-uploads";

    private UploadPathUtil() {
    }

    public static Path resolveUploadDirectory(ServletContext context) {
        if (context != null) {
            String realPath = context.getRealPath("/uploads");
            if (realPath != null && !realPath.isBlank()) {
                Path webappDir = Paths.get(realPath).toAbsolutePath().normalize();
                try {
                    Files.createDirectories(webappDir);
                    return webappDir;
                } catch (Exception ignored) {
                    // fall through
                }
            }
        }
        Path projectDir = Paths.get(System.getProperty("user.dir"), "uploads", SUB_DIR)
                .toAbsolutePath().normalize();
        try {
            Files.createDirectories(projectDir);
        } catch (Exception e) {
            throw new IllegalStateException("Cannot create upload directory: " + projectDir, e);
        }
        return projectDir;
    }

    public static Path resolveStoredFile(ServletContext context, String storedName) {
        return resolveUploadDirectory(context).resolve(safeFileName(storedName));
    }

    public static String safeFileName(String name) {
        if (name == null) {
            return "file";
        }
        return Paths.get(name).getFileName().toString().replaceAll("[\\\\/:*?\"<>|]", "_");
    }
}
